package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.DeviceResource;
import com.mthoko.mobile.resource.internal.MemberResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.DeviceResourceRemote;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.List;
import java.util.Map;

public class DeviceServiceImpl extends BaseServiceImpl<Device> implements DeviceService {

    private final DeviceResource deviceResource;

    private final DeviceResourceRemote deviceResourceRemote;

    private final MemberResource memberResource;

    public DeviceServiceImpl(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper();
        deviceResource = new DeviceResource(context, databaseWrapper);
        memberResource = new MemberResource(context, databaseWrapper);
        deviceResourceRemote = new DeviceResourceRemote(context, new ConnectionWrapper(null));
    }

    @Override
    public void setContext(Context context) {
        deviceResource.setContext(context);
        memberResource.setContext(context);
    }

    public Device findByImei(String imei) {
        Device device = deviceResource.findByImei(imei);
        return device;
    }

    public Device getCurrentDevice() {
        String imei = getImei();
        Device device = findByImei(imei);
        if (device == null) {
            device = new Device();
            device.setImei(imei);
        }
        return device;
    }

    @Override
    public BaseResource getResource() {
        return deviceResource;
    }

    @Override
    public Context getContext() {
        return deviceResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return deviceResourceRemote;
    }

    public void saveExternally(long externalMemberId, Device device) {
        deviceResourceRemote.save(externalMemberId, device);
        if (deviceResource.existsWith(device.getId())) {
            update(device);
        }
    }

    public boolean assignOwner(long memberId, Device device) {
        boolean updated = false;
        if (memberResource.existsWith(memberId)) {
            device.setMemberId(memberId);
            update(device);
            updated = true;
        }

        return updated;
    }

    public Device findRemoteDeviceByImei(String imei) {
        return deviceResourceRemote.findByImei(imei);
    }

    public Map<String, Long> retrieveVerification(Device device) {
        return deviceResourceRemote.retrieveVerificationByImei(device.getImei());
    }

    public List<Device> findByMemberId(Long memberId) {
        return deviceResource.findByMemberId(memberId);
    }

    public Long findMemberIdByImei(String imei) {
        return deviceResource.findMemberIdByImei(imei);
    }
}
