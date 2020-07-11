package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.Device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceResourceApi extends BaseResourceApi<Device> {

    public DeviceResourceApi(Context context) {
        super(context, Device.class);
    }

    public long save(Long memberId, Device device) {
        device.setMemberId(memberId);
        return save(device);
    }

    public List<Device> findByMemberId(long memberId) {
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_DEVICES_BY_MEMBER_ID);
        params.put("value", String.valueOf(memberId));
        return retrieveByRequest(params);
    }

    public Device findByImei(String imei) {
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_DEVICE_BY_IMEI);
        params.put("value", '"' + imei + '"');
        return retrieveOneByRequest(params);
    }

    public Device retrieveVerificationByImei(String imei) {
        return null;
    }
}