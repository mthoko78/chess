package com.mthoko.mobile.service.proxy;

import java.util.List;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.LocationStampServiceImpl;

public class LocationStampServiceProxy extends BaseServiceImpl implements LocationStampService {

	private final LocationStampServiceImpl service;

	public LocationStampServiceProxy() {
		service = new LocationStampServiceImpl();
	}

	public List<LocationStamp> findByImei(String imei) {
		boolean connection = service.openConnection();
		List<LocationStamp> device = service.findByImei(imei);
		service.closeConnectionIf(connection);
		return device;
	}

	@Override
	public BaseResourceRemote<LocationStamp> getResource() {
		return service.getResource();
	}

	@Override
	public LocationStamp findMostRecentByImei(String imei) {
		boolean connection = service.openConnection();
		LocationStamp locationStamp = service.findMostRecentByImei(imei);
		service.closeConnectionIf(connection);
		return locationStamp;
	}

}
