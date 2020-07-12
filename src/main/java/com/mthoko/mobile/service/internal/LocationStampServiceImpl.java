package com.mthoko.mobile.service.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.LocationStampResourceRemote;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class LocationStampServiceImpl extends BaseServiceImpl<LocationStamp> implements LocationStampService {

    private final LocationStampResourceRemote locationStampResourceRemote;

    public LocationStampServiceImpl() {
        locationStampResourceRemote = new LocationStampResourceRemote(new ConnectionWrapper(null));
    }

    public List<LocationStamp> findRemoteLocationStampsByImei(String imei) {
        return locationStampResourceRemote.findByImei(imei);
    }

    @Override
    public BaseResourceRemote getResource() {
        return locationStampResourceRemote;
    }


    public Map<String, Long> retrieveVerification(LocationStamp device) {
        return locationStampResourceRemote.retrieveVerificationByImei(device.getImei());
    }

    @Override
    public List<Long> saveToRemote(List<LocationStamp> locationStamps) {
        return locationStampResourceRemote.saveAll(locationStamps);
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
}