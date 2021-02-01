package com.mthoko.mobile.location;

import java.util.List;

import com.mthoko.mobile.common.BaseService;

public interface LocationStampService extends BaseService<LocationStamp> {

	List<LocationStamp> findByImei(String imei);

	LocationStamp findMostRecentByImei(String imei);

	Integer countByImei(String imei);

	String unsetProperty(String key);

}
