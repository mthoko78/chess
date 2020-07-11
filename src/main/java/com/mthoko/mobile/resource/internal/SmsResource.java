package com.mthoko.mobile.resource.internal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsResource extends BaseResource<Sms> {

    public static final String INBOX = "content://sms/inbox";
    public static final String SENT = "content://sms/sent";
    public static final String DRAFT = "content://sms/draft";
    public static final String SIM = "content://sms/icc";

    public SmsResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, Sms.class,databaseWrapper);
    }

    public List<Sms> getActualInbox() {
        Cursor cursor = getContext().getContentResolver().query(Uri.parse(INBOX), null, null, null, null);
        return extractFromInternalCursor(cursor);
    }

    public List<Sms> getActualSimInbox() {
        Cursor cursor = getContext().getContentResolver().query(Uri.parse(SIM), null, null, null, null);
        return extractFromInternalCursor(cursor);
    }

    public List<Sms> getActualSent() {
        Cursor cursor = getContext().getContentResolver().query(Uri.parse(SENT), null, null, null, null);
        return extractFromInternalCursor(cursor);
    }

    public List<Sms> getActualDrafts() {
        Cursor cursor = getContext().getContentResolver().query(Uri.parse(DRAFT), null, null, null, null);
        return extractFromInternalCursor(cursor);
    }

    public List<Sms> extractFromInternalCursor(Cursor cursor) {
        List<Sms> smsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
                do {
                    smsList.add(extractOneFromInternalCursor(cursor));
                } while (cursor.moveToNext());
        }
        return smsList;
    }

    private Sms extractOneFromInternalCursor(Cursor cursor) {
//        String columnNames = Arrays.deepToString(cursor.getColumnNames());
        Map<String, String> results = new HashMap<>();
        for (
                String columnName : cursor.getColumnNames()) {
            results.put("\n" + columnName, cursor.getString(cursor.getColumnIndex(columnName)));
        }
        int readIndex = cursor.getColumnIndex("read");
        int seenIndex = cursor.getColumnIndex("seen");
        int dateSentIndex = cursor.getColumnIndex("date_sent");
        if (readIndex == -1) {
            readIndex = cursor.getColumnIndex("status");
        }
        if (seenIndex == -1) {
            seenIndex = cursor.getColumnIndex("status_on_icc");
        }
        if (dateSentIndex == -1) {
            dateSentIndex = cursor.getColumnIndex("date");
        }

        Sms sms = new Sms();
        sms.setRecipient(super.getPhoneNumber());
        sms.setRead(cursor.getInt(readIndex) > 0);
        sms.setBody(cursor.getString(cursor.getColumnIndex("body")));
        sms.setDate(parseDate(cursor.getString(cursor.getColumnIndex("date"))));
        sms.setDateSent(parseDate(cursor.getString(dateSentIndex)));
        sms.setSender(cursor.getString(cursor.getColumnIndex("address")));
//        sms.setDelivered(cursor.getInt(seenIndex) > 0);
        sms.setDeliveryReportRequested(false);
        sms.setLocation(null);
        sms.setSent(true);
        return sms;
    }

    private Date parseDate(String dateStr) {
        if (dateStr.matches("\\d+")) {
            return new Date(Long.parseLong(dateStr));
        }
        return null;
    }

    public List<Sms> getInbox(String recipient) {
        return findWhere(String.format("recipient = '%s'", recipient));
    }

    public List<Sms> getSent(String sender) {
        return findWhere(String.format("sender = '%s'", sender));
    }

    public void viewColumnNames() {

        String[] devSmsColumns = {
                "_id", "thread_id", "address", "person", "date", "date_sent", "protocol", "read",
                "status", "type", "reply_path_present", "subject", "body", "service_center", "locked",
                "sub_id", "error_code", "creator", "seen"};

        String[] simSmsColumns = {
                "service_center_address",
                "address",
                "message_class",
                "body",
                "date, ",
                "status",
                "status_on_icc",
                "index_on_icc",
                "is_status_report",
                "transport_type",
                "type",
                "locked",
                "error_code, ",
                "_id"
        };
        String[] sentSmsCols = new String[]{
                "_id",
                "thread_id",
                "address",
                "person",
                "date",
                "date_sent",
                "protocol",
                "read",
                "status",
                "type",
                "reply_path_present",
                "subject",
                "body",
                "service_center",
                "locked",
                "sub_id",
                "network_type",
                "error_code",
                "creator",
                "seen",
                "si_id",
                "mid",
                "created",
                "mtype",
                "privacy_mode",
                "group_id",
                "addr_body",
                "time_body",
                "risk_url_body",
                "is_secret, resent_im"
        };
    }

    public List<Sms> findByPhoneOrSimNo(String phone, String simNo) {
        return findWhere(String.format("recipient = '%s' OR recipient = '%s'", phone, simNo));
    }

    public List<Sms> findByPhone(String phone) {
        return findByRecipient(phone);
    }

    public List<Sms> findBySimNo(String simNo) {
        return findByRecipient(simNo);
    }

    public List<Sms> findByRecipient(String recipient) {
        return findWhere(String.format("recipient = '%s'", recipient));
    }

    public List<Sms> findUnverifiedBySimNo(String simNo) {
        return findUnverifiedByRecipient(simNo);
    }

    public List<Sms> findUnverifiedByPhone(String phone) {
        return findUnverifiedByRecipient(phone);
    }

    public List<Sms> findUnverifiedByRecipient(String recipient) {
        String entityName = getEntityName();
        String whereClause = String.format("%s.recipient = '%s' AND %s.verificationId IS NULL", entityName, recipient, entityName);
        return super.findWhere(whereClause);
    }

    public List<Long> retrieveVerificationIdsByRecipient(String recipient) {
        String entityName = getEntityName();
        String whereClause = String.format("%s.recipient = '%s' AND %s.verificationId IS NOT NULL", entityName, recipient, entityName);
        return retrieveVerificationIdsWhere(whereClause);
    }

    public List<Sms> getSmsListFromBundle(Bundle bundle) {
        Object[] pdu_Objects = (Object[]) bundle.get("pdus");
        final List<Sms> smsList = new ArrayList<>();
        if (pdu_Objects != null) {
            for (Object aObject : pdu_Objects) {
                Sms currentSms = getIncomingMessage(aObject, bundle);
                smsList.add(currentSms);
            }
        }
        return smsList;
    }

    public Sms getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        Sms sms = new Sms();
        sms.setBody(currentSMS.getMessageBody());
        sms.setSender(currentSMS.getDisplayOriginatingAddress());
        sms.setRecipient(null);
        sms.setDate(new Date(currentSMS.getTimestampMillis()));
        sms.setDateSent(new Date(currentSMS.getTimestampMillis()));
        return sms;
    }
}
