package com.mthoko.mobile.service.internal;

import java.util.Map;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.DeviceResourceRemote;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class DeviceServiceImpl extends BaseServiceImpl<Device> implements DeviceService {


    private final DeviceResourceRemote deviceResourceRemote;


    public DeviceServiceImpl() {
        deviceResourceRemote = new DeviceResourceRemote(new ConnectionWrapper(null));
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return deviceResourceRemote;
    }

    public void saveExternally(long externalMemberId, Device device) {
        deviceResourceRemote.save(externalMemberId, device);
    }

    public Device findRemoteDeviceByImei(String imei) {
        return deviceResourceRemote.findByImei(imei);
    }

    public Map<String, Long> retrieveVerification(Device device) {
        return deviceResourceRemote.retrieveVerificationByImei(device.getImei());
    }

}