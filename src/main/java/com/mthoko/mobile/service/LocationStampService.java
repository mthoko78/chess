package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.LocationStamp;

import java.util.List;

public interface LocationStampService extends BaseService {

	List<LocationStamp> findByImei(String imei);

	LocationStamp findMostRecentByImei(String imei);

}
