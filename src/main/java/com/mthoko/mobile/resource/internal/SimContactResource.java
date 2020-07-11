package com.mthoko.mobile.resource.internal;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;

public class SimContactResource extends BaseResource<SimContact> {

    public static final String SIM_CONTACTS_URI = "content://icc/adn";

    public SimContactResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, SimContact.class, databaseWrapper);
    }

    public SimContact findByPhone(String phone) {
        String whereClause = String.format("phone = '%s'", phone);
        if (phone.length() > 9 && (phone.startsWith("+27") || phone.startsWith("27") || phone.charAt(0) == '0')) {
            String last9Digits = phone.substring(phone.length() - 9);
            whereClause = String.format("phone IN('%s', '%s', '%s')", "+27" + last9Digits, "27" + last9Digits, "0" + last9Digits);
        }
        return findOneWhere(getEntityName() + "." + whereClause);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public List<SimContact> getActualSimContacts() {
        List<SimContact> result = new ArrayList<>();
        Uri simUri = Uri.parse(SIM_CONTACTS_URI);
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursorSim = contentResolver.query(simUri, null, null, null, null);
        while (cursorSim.moveToNext()) {
            String name = cursorSim.getString(cursorSim.getColumnIndex("name"));
            String phone = cursorSim.getString(cursorSim.getColumnIndex("number"));
            if (phone != null) {
                phone.replaceAll("[\\D]+", "");
            }
            if (name != null) {
                name = name.replace("|", "");
            } else {
                Log.e("NULL NAME WITH PHONE", "" + phone);
                name = phone;
            }
            result.add(new SimContact(name, phone));
        }
        return result;
    }

    public List<Long> retrieveVerificationIdsBySimNo(String simNo) {
        String name = SimCard.class.getSimpleName();
        String whereClause = String.format("%s.simNo = '%s' AND %s.verificationId IS NOT NULL", name, simNo, getEntityName());
        return retrieveVerificationIdsJoiningWhere(SimCard.class, SimContact.class, whereClause);
    }

    public void saveToCurrentSim(List<SimContact> simContacts) {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri simUri = Uri.parse(SIM_CONTACTS_URI);
        Cursor idCursor = contentResolver.query(simUri, new String[]{"MAX(_id) AS _id"}, null, null, null);
        Cursor adnCursor = contentResolver.query(simUri, new String[]{"MAX(adn_index) AS adn_index"}, null, null, null);
//        columnNames = new String[]{"name", "number", "anr_number", "anrA_number", "anrB_number", "anrC_number", "emails", "adn_index", "_id"};
        int currentId = 1;
        int currentAdnIndex = 1;
        if (idCursor.moveToFirst()) {
            currentId += idCursor.getInt(idCursor.getColumnIndex("_id"));
        }
        if (adnCursor.moveToFirst()) {
            currentAdnIndex += idCursor.getInt(0);
        }
        idCursor.close();
        adnCursor.close();
        String emails = ",";
        ContentValues[] contentValuesArray = new ContentValues[simContacts.size()];
        for (int i = 0; i < simContacts.size(); i++) {
            contentValuesArray[i] = getContentValues(simContacts.get(i), emails, currentId++, currentAdnIndex++);
        }
        int insert = contentResolver.bulkInsert(simUri, contentValuesArray);
        contentResolver.notifyChange(simUri, null);
    }

    public List<SimContact> findBySimCardId(Long simCardId) {
        return findWhere(getEntityName() + ".simCardId = " + simCardId);
    }

    public List<SimContact> findUnverifiedBySimNo(String simNo) {
        String name = SimCard.class.getSimpleName();
        String whereClause = String.format("%s.simNo = '%s' AND %s.verificationId IS NULL", name, simNo, getEntityName());
        return super.findWhereJoining(SimCard.class, SimContact.class, whereClause);
    }

    public void save(Long simCardId, List<SimContact> contacts) {
        for (SimContact contact : contacts) {
            contact.setSimCardId(simCardId);
        }
        saveAll(contacts);
    }

    public List<SimContact> findBySimNo(String simNo) {
        String whereClause = String.format("%s.%s = '%s'", SimCard.class.getSimpleName(), "simNo", simNo);
        return super.findWhereJoining(SimCard.class, SimContact.class, whereClause);
    }

    public Long save(Long simCardId, String name, String phone) {
        return save(new SimContact(simCardId, name, phone));
    }

    private ContentValues getContentValues(SimContact simContact, String emails, int id, int adnIndex) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", simContact.getName());
        contentValues.put("tag", simContact.getName());
        contentValues.put("number", simContact.getPhone());
        contentValues.put("emails", emails);
        contentValues.put("_id", id);
        contentValues.put("adn_index", adnIndex);
        return contentValues;
    }

}
