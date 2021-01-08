package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.LocationStamp;

public interface LocationStampService extends BaseService<LocationStamp> {

	List<LocationStamp> findByImei(String imei);

	LocationStamp findMostRecentByImei(String imei);

	Integer countByImei(String imei);

	String unsetProperty(String key);

}
