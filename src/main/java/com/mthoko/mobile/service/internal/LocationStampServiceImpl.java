package com.mthoko.mobile.service.internal;

import android.content.Context;
import android.location.Location;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.LocationStampResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.LocationStampResourceRemote;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class LocationStampServiceImpl extends BaseServiceImpl<LocationStamp> implements LocationStampService {

    private final LocationStampResource locationStampResource;

    private final LocationStampResourceRemote locationStampResourceRemote;

    public LocationStampServiceImpl(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper();
        locationStampResource = new LocationStampResource(context, databaseWrapper);
        locationStampResourceRemote = new LocationStampResourceRemote(context, new ConnectionWrapper(null));
    }

    @Override
    public void setContext(Context context) {
        locationStampResource.setContext(context);
    }

    public List<LocationStamp> findByImei(String imei) {
        return locationStampResource.findByImei(imei);
    }

    public List<LocationStamp> findRemoteLocationStampsByImei(String imei) {
        return locationStampResourceRemote.findByImei(imei);
    }

    @Override
    public BaseResource getResource() {
        return locationStampResource;
    }

    @Override
    public Context getContext() {
        return locationStampResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return locationStampResourceRemote;
    }


    public Map<String, Long> retrieveVerification(LocationStamp device) {
        return locationStampResourceRemote.retrieveVerificationByImei(device.getImei());
    }

    @Override
    public List<Long> saveToRemote(List<LocationStamp> locationStamps) {
        return locationStampResourceRemote.saveAll(locationStamps);
    }

    @Override
    public void updateMostRecentLatLongsByImei(String imei, final List<Location> locations) {
//        List<LocationStamp> locationStamps = filterByDate(locations.size(), locationStampResourceRemote.findByImei(imei));
//        List<Location> processedLocations = new ArrayList<>();
//        for (int i = 0; i <= locationStamps.size(); i++) {
//            Location location = locations.get(i);
//            LocationStamp locationStamp = locationStamps.get(i);
//            locationStamp.setLatitude(String.valueOf(location.getLatitude()));
//            locationStamp.setLongitude(String.valueOf(location.getLongitude()));
//            locationStamp.setTimeCaptured(new Date());
//            processedLocations.add(location);
//        }
//        updateAll(locationStamps);
//        locations.removeAll(processedLocations);
//        if (locations.size() > 0) {
//            locationStampResourceRemote.saveAll(locationsToLocationStamps(imei, locations));
//        }
        locationStampResourceRemote.saveAll(locationsToLocationStamps(imei, locations));
    }

    public List<LocationStamp> locationsToLocationStamps(String imei, List<Location> locations) {
        String deviceName = getDeviceName();
        String currentSimNo = getCurrentSimNo();
        List<LocationStamp> newLocationStamps = new ArrayList<>();
        for (Location location : locations) {
            LocationStamp locationStamp = new LocationStamp(
                    imei,
                    deviceName,
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()),
                    new Date(),
                    currentSimNo);
            newLocationStamps.add(locationStamp);
        }
        return newLocationStamps;
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