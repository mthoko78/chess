package com.mthoko.mobile.domain.locationstamp;

import com.mthoko.mobile.common.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface LocationStampService extends BaseService<LocationStamp> {

	List<LocationStamp> findByImei(String imei);

	Optional<LocationStamp> findMostRecentByImei(String imei);

	Integer countByImei(String imei);

	String unsetProperty(String key);

}
