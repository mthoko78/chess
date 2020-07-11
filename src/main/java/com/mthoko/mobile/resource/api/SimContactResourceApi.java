package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.exception.ApplicationException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimContactResourceApi extends BaseResourceApi<SimContact> {

    public SimContactResourceApi(Context context) {
        super(context, SimContact.class);
    }

    public int countSimContactsBySimNo(String simNo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", COUNT_SIM_CONTACTS_BY_SIM_NO);
        params.put("value", '"' + simNo + '"');
        return Integer.parseInt(getResponseText(params));
    }

    public List<Long> save(String simNo, List<SimContact> contacts) {
        SimCardResourceApi simCardResource = new SimCardResourceApi(getContext());
        SimCard simCard = simCardResource.findBySimNo(simNo);
        List<Long> localSimIds = getValues(contacts, "simCardId", Long.class);
        setValue(contacts, simCard.getId(), "simCardId");
        List<Long> newIds = super.saveAll(contacts);
        setValues(contacts, localSimIds, "simCardId");
        List<Long> verificationIds = getValues(contacts, "id", Long.class);
        setValues(contacts, newIds, "verificationId");
        return newIds;
    }

    public List<SimContact> findBySimCardId(Long simCardId) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("key", FIND_SIM_CONTACTS_BY_SIM_CARD_ID);
        params.put("value", String.valueOf(simCardId));
        return retrieveByRequest(params);
    }

    public List<SimContact> findBySimNo(String simNo) {
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_SIM_CONTACTS_BY_SIM_NO);
        params.put("value", '"' + simNo + '"');
        return retrieveByRequest(params);
    }

    public List<SimContact> findBySimNoWithIdsNotIn(List<Long> ids, String simNo) {
        if (ids.isEmpty()) {
            return findBySimNo(simNo);
        }
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_SIM_CONTACTS_WITH_IDS_NOT_IN);
        params.put("value", ids.toString());
        params.put("simNo", simNo);
        return retrieveByRequest(params);
    }

    public Map<String, Long> retrieveVerificationBySimNo(String simNo) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("key", RETRIEVE_SIM_CONTACTS_VERIFICATION_BY_SIM_NO);
        params.put("value", '"' + simNo + '"');
        String urlData = getResponseText(params);
        try {
            JSONObject jsonObject = new JSONObject(urlData);
            return extractVerificationFromJson(jsonObject);
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }
}
