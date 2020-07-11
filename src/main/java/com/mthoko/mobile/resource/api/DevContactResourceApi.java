package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.exception.ApplicationException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevContactResourceApi extends BaseResourceApi<DevContact> {

    public DevContactResourceApi(Context context) {
        super(context, DevContact.class);
    }

    public List<Long> save(String imei, List<DevContact> contacts) {
        DeviceResourceApi deviceResource = new DeviceResourceApi(getContext());
        Device device = deviceResource.findByImei(imei);
        if (device != null) {
            String devId = "devId";
            List<Long> localSimIds = getValues(contacts, devId, Long.class);
            setValue(contacts, device.getId(), devId);
            List<Long> newIds = saveAll(contacts);
            setValues(contacts, localSimIds, devId);
            return newIds;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Long> saveAll(List<DevContact> devContacts) {
        List<Long> ids = super.saveAll(devContacts);
        if (ids.size() > devContacts.size()) {
            List<Long> contactValuesIds = ids.subList(devContacts.size(), ids.size());
            int i = 0;
            for (DevContact contact : devContacts) {
                for (DevContactValue value : contact.getValues()) {
                    if (i < contactValuesIds.size()) {
                        value.setVerificationId(contactValuesIds.get(i++));
                    }
                }
            }
        }
        return ids;
    }

    public List<DevContact> findByImei(String imei) {
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_DEVICE_CONTACTS_BY_IMEI);
        params.put("value", '"' + imei + '"');
        return retrieveByRequest(params);
    }

    public List<DevContact> findByImeiWithIdsNotIn(List<Long> ids, String imei) {
        if (ids.isEmpty()) {
            return findByImei(imei);
        }
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_DEV_CONTACTS_WITH_IDS_NOT_IN);
        params.put("value", ids.toString());
        params.put("imei", imei);
        return retrieveByRequest(params);
    }

    public int countByImei(String imei) {
        Map<String, String> params = new HashMap<>();
        params.put("key", COUNT_DEV_CONTACTS_BY_IMEI);
        params.put("value", '"' + imei + '"');
        return retrieveIntByRequest(params);
    }

    public Map<String, Long> retrieveVerificationByImei(String imei) {
        Map<String, String> params = new HashMap<>();
        params.put("key", RETRIEVE_DEV_CONTACTS_VERIFICATION_BY_IMEI);
        params.put("value", '"' + imei + '"');
        try {
            String responseText = getResponseText(params);
            return extractVerificationFromJson(new JSONObject(responseText));
        } catch (JSONException e) {
            throw new ApplicationException(e);
        }
    }
}