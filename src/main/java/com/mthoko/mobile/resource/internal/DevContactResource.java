package com.mthoko.mobile.resource.internal;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevContactResource extends BaseResource<DevContact> {

    private static final String[] SELECTION_COLUMNS = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };


    public DevContactResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, DevContact.class, databaseWrapper);
    }

    public void saveOnDevice(DevContact contact) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName()).build());

        for (Map.Entry<Integer, String> phone : contact.getPhones().entrySet()) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getValue())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phone.getKey())
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            throw new ApplicationException(e);
        }
    }

    public List<DevContact> getActualDevContacts() {
        ContentResolver contentResolver = getContext().getContentResolver();
        List<DevContact> contacts = retrieveDevContactsExcludingValues(contentResolver);
        Map<Long, List<DevContactValue>> allPhoneNumbers = retrieveAllPhoneNumbersGroupedByContactIds(contentResolver);
        for (DevContact contact : contacts) {
            List<DevContactValue> values = allPhoneNumbers.get(contact.getId());
            if (values != null) {
                contact.setValues(values);
            }
        }
        return contacts;
    }

    private List<DevContact> retrieveDevContactsExcludingValues(ContentResolver contentResolver) {
        Uri URI = ContactsContract.RawContacts.CONTENT_URI;
        String columnName = ContactsContract.RawContacts.ACCOUNT_TYPE;
        Cursor cursor = contentResolver.query(
                URI,
                null,
                columnName + " LIKE ?",
                new String[]{"%phone%"},
                null);
        List<DevContact> accounts = new ArrayList<>();
        while (cursor.moveToNext()) {
            accounts.add(extractDevContactFromCursor(cursor));
        }
        return accounts;
    }

    private Map<Long, List<DevContactValue>> retrieveAllPhoneNumbersGroupedByContactIds(ContentResolver contentResolver) {
        final Uri URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        final String NUMBER_COL_NAME = ContactsContract.CommonDataKinds.Phone.NUMBER;
        final String TYPE_COL_NAME = ContactsContract.CommonDataKinds.Phone.TYPE;
        final String CONTACT_ID_COL = ContactsContract.Data.RAW_CONTACT_ID;
        final String ACCOUNT_TYPE_AND_DATA_SET = ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET;
        String[] projection = {CONTACT_ID_COL, NUMBER_COL_NAME, TYPE_COL_NAME};
        Cursor phoneCursor = contentResolver.query(
                URI,
                projection,
                ACCOUNT_TYPE_AND_DATA_SET + " LIKE ?",
                new String[]{"%phone%"},
                null
        );
        Map<Long, List<DevContactValue>> phoneNumbers = new HashMap<>();
        while (phoneCursor.moveToNext()) {
            String number = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER_COL_NAME));
            int strType = phoneCursor.getInt(phoneCursor.getColumnIndex(TYPE_COL_NAME));
            long contactId = (long) phoneCursor.getInt(phoneCursor.getColumnIndex(CONTACT_ID_COL));
            if (!phoneNumbers.containsKey(contactId)) {
                phoneNumbers.put(contactId, new ArrayList<DevContactValue>());
            }
            phoneNumbers.get(contactId).add(new DevContactValue(contactId, strType, number));
        }
        phoneCursor.close();
        return phoneNumbers;
    }

    public List<DevContact> findByDeviceId(long deviceId) {
        return super.findWhere(getEntityName() + ".devId = " + deviceId);
    }

    public List<DevContact> findByImei(String imei) {
        String whereClause = String.format("%s.imei = '%s'", Device.class.getSimpleName(), imei);
        List<DevContact> contacts = super.findWhereJoining(Device.class, DevContact.class, whereClause);
        return contacts;
    }

    private DevContact extractDevContactFromCursor(Cursor contactsCursor) {
        DevContact contact = new DevContact();
        Long id = contactsCursor.getLong(contactsCursor.getColumnIndex(ContactsContract.RawContacts._ID));
        String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        contact.setName(name);
        contact.setId(id);
        return contact;
    }

    private List<DevContactValue> getEmails(Integer id, ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{String.valueOf(id)}, null);
        List<DevContactValue> emails = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Integer emailType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
                if (emailType == null) {
                    emailType = 1;
                }
                emails.add(new DevContactValue(null, emailType, email));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return emails;
    }

    public void saveAllOnDevice(List<DevContact> devContacts) {
        for (DevContact devContact : devContacts) {
            saveOnDevice(devContact);
        }
    }

    public List<DevContact> findUnverifiedByImei(String imei) {
        String name = Device.class.getSimpleName();
        String whereClause = String.format("%s.imei = '%s' AND %s.verificationId IS NULL", name, imei, getEntityName());
        return super.findWhereJoining(Device.class, DevContact.class, whereClause);
    }

    public List<Long> retrieveVerificationIdsByImei(String imei) {
        String name = Device.class.getSimpleName();
        String whereClause = String.format("%s.imei = '%s' AND %s.verificationId IS NOT NULL", name, imei, getEntityName());
        return retrieveVerificationIdsJoiningWhere(Device.class, DevContact.class, whereClause);
    }
}