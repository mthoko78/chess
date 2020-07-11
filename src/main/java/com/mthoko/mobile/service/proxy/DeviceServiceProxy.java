package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.DeviceServiceImpl;

public class DeviceServiceProxy extends BaseServiceImpl<Device> implements DeviceService {

    private final DeviceServiceImpl service;

    public DeviceServiceProxy(Context context) {
        service = new DeviceServiceImpl(context);
    }

    @Override
    public Long findMemberIdByImei(String imei) {
        boolean connection = service.openConnection();
        Long id = service.findMemberIdByImei(imei);
        service.closeConnectionIf(connection);
        return id;
    }

    @Override
    public Device findRemoteDeviceByImei(String imei) {
        boolean connection = service.openRemoteConnection();
        Device device = service.findRemoteDeviceByImei(imei);
        service.closeRemoteConnectionIf(connection);
        return device;
    }

    @Override
    public Device findByImei(String imei) {
        boolean connection = service.openConnection();
        Device device = service.findByImei(imei);
        service.closeConnectionIf(connection);
        return device;
    }

    @Override
    public BaseResource getResource() {
        return service.getResource();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

    @Override
    public void setContext(Context context) {
        service.setContext(context);
    }
}
