package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.LocationStamp;

import java.util.List;

public interface LocationStampService extends BaseService<LocationStamp> {

    List<LocationStamp> findRemoteLocationStampsByImei(String imei);

    List<Long> saveToRemote(List<LocationStamp> locationStamps);

}
