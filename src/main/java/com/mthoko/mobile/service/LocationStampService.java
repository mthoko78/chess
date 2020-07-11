package com.mthoko.mobile.service;

import android.location.Location;

import com.mthoko.mobile.entity.LocationStamp;

import java.util.List;

public interface LocationStampService extends BaseService<LocationStamp> {

    List<LocationStamp> findByImei(String imei);

    List<LocationStamp> findRemoteLocationStampsByImei(String imei);

    List<Long> saveToRemote(List<LocationStamp> locationStamps);

    void updateMostRecentLatLongsByImei(String imei, List<Location> locations);
}
