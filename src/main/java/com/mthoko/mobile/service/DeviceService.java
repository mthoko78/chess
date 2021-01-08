package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.Device;

public interface DeviceService extends BaseService<Device> {

    Device findByImei(String imei);

	List<Device> findByMemberId(Long memberId);

}
