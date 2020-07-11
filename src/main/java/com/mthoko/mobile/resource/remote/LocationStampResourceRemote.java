package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.util.ConnectionWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationStampResourceRemote extends BaseResourceRemote<LocationStamp> {

	public LocationStampResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
		super(LocationStamp.class, context, connectionWrapper);
	}

	public List<LocationStamp> findByImei(String imei) {
		return findWhere(String.format("%s.imei = '%s'", getEntityName(), imei));
	}

	public Map<String, Long> retrieveVerificationByImei(String imei) {
		List<LocationStamp> locationStamps = findByImei(imei);
		if (locationStamps != null) {
			return extractVerification(locationStamps);
		}
		return new HashMap<>();
	}
}