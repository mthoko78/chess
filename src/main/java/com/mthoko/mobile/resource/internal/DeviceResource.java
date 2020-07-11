package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.List;

public class DeviceResource extends BaseResource<Device> {

    public DeviceResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, Device.class, databaseWrapper);
    }

    public Device findByImei(String imei) {
        return findOneWhere(String.format(getEntityName() + ".imei = '%s'", imei));
    }

    public List<Device> findByMemberId(Long memberId) {
        return findWhere(String.format(getEntityName() + ".memberId = %s", memberId));
    }

    public Long findMemberIdByImei(String imei) {
        Device device = findByImei(imei);
        if (device != null) {
            return device.getMemberId();
        }
        return null;
    }
}