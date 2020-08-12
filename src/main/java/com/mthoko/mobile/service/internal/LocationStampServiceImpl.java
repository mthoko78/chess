package com.mthoko.mobile.service.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.resource.LocationStampResourceRemote;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class LocationStampServiceImpl extends BaseServiceImpl implements LocationStampService {

	private final LocationStampResourceRemote locationStampResourceRemote;

	public LocationStampServiceImpl() {
		locationStampResourceRemote = new LocationStampResourceRemote(new ConnectionWrapper(null));
	}

	public List<LocationStamp> findByImei(String imei) {
		return locationStampResourceRemote.findByImei(imei);
	}

	@Override
	public BaseResourceRemote<LocationStamp> getResource() {
		return locationStampResourceRemote;
	}

	public Map<String, Long> retrieveVerification(LocationStamp device) {
		return locationStampResourceRemote.retrieveVerificationByImei(device.getImei());
	}

	private List<LocationStamp> filterByDate(int max, List<LocationStamp> locationStamps) {
		SortedMap<Long, LocationStamp> sortedMap = new TreeMap<>();
		for (LocationStamp locationStamp : locationStamps) {
			sortedMap.put(locationStamp.getTimeCaptured().getTime(), locationStamp);
		}
		List<LocationStamp> filtered = new ArrayList<>();
		while (!sortedMap.isEmpty() && filtered.size() < max) {
			filtered.add(0, sortedMap.remove(sortedMap.lastKey()));
		}
		return filtered;
	}

	@Override
	public LocationStamp findMostRecentByImei(String imei) {
		// TODO Auto-generated method stub
		return locationStampResourceRemote.findMostRecentByImei(imei);
	}
}