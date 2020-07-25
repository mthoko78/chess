package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.LocationStamp;

import java.util.List;

public interface LocationStampService extends BaseService<LocationStamp> {

    List<LocationStamp> findByImei(String imei);

    List<Long> saveToRemote(List<LocationStamp> locationStamps);

	LocationStamp findMostRecentByImei(String imei);

}
