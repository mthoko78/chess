package com.mthoko.mobile.domain.location;

import com.mthoko.mobile.common.BaseService;

import java.util.List;

public interface LocationStampService extends BaseService<LocationStamp> {

	List<LocationStamp> findByImei(String imei);

	LocationStamp findMostRecentByImei(String imei);

	Integer countByImei(String imei);

	String unsetProperty(String key);

}
