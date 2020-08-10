package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.Device;

public interface DeviceService extends BaseService {

    Device findRemoteDeviceByImei(String imei);

}
