package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.Device;

public interface DeviceService extends BaseService<Device> {

    Long findMemberIdByImei(String imei);

    String getImei();

    Device findRemoteDeviceByImei(String imei);

    Device findByImei(String imei);
}
