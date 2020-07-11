package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.exception.ApplicationException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SmsResourceApi extends BaseResourceApi<Sms> {

    public SmsResourceApi(Context context) {
        super(context, Sms.class);
    }

    public int countSmsesByRecipient(String recipient) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", COUNT_SMSES_BY_RECIPIENT);
        params.put("value", '"' + recipient + '"');
        return Integer.parseInt(getResponseText(params));
    }

    public Map<String, Long> retrieveVerificationByRecipient(String recipient) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("key", RETRIEVE_SMSES_VERIFICATION_BY_RECIPIENT);
        params.put("value", '"' + recipient + '"');
        String urlData = getResponseText(params);
        try {
            JSONObject jsonObject = new JSONObject(urlData);
            return extractVerificationFromJson(jsonObject);
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }

    public List<Sms> findByRecipientWithIdsNotIn(List<Long> ids, String recipient) {
        if (ids.isEmpty()) {
            return findByRecipient(recipient);
        }
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_SMSES_WITH_IDS_NOT_IN);
        params.put("value", ids.toString());
        params.put("recipient", recipient);
        return retrieveByRequest(params);
    }

    public List<Sms> findByRecipient(String recipient) {
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_SMSES_BY_RECIPIENT);
        params.put("value", '"' + recipient + '"');
        return retrieveByRequest(params);
    }
}
