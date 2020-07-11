package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.LocationStampServiceImpl;

import java.util.List;

public class LocationStampServiceProxy extends BaseServiceImpl<LocationStamp> implements LocationStampService {

    private final LocationStampServiceImpl service;

    public LocationStampServiceProxy() {
        service = new LocationStampServiceImpl();
    }

    public List<LocationStamp> findRemoteLocationStampsByImei(String imei) {
        boolean connection = service.openRemoteConnection();
        List<LocationStamp> device = service.findRemoteLocationStampsByImei(imei);
        service.closeRemoteConnectionIf(connection);
        return device;
    }

    @Override
    public List<Long> saveToRemote(List<LocationStamp> locationStamps) {
        boolean openConnection = service.openRemoteConnection();
        boolean transaction = service.beginRemoteTransaction();
        List<Long> ids = service.saveToRemote(locationStamps);
        service.endRemoteTransactionIf(transaction);
        service.closeRemoteConnectionIf(openConnection);
        return ids;
    }

    @Override
    public void updateMostRecentLatLongsByImei(String imei, List<Location> locations) {
        boolean openConnection = service.openRemoteConnection();
        boolean transaction = service.beginRemoteTransaction();
        service.updateMostRecentLatLongsByImei(imei, locations);
        service.endRemoteTransactionIf(transaction);
        service.closeRemoteConnectionIf(openConnection);
    }

    @Override
    public List<LocationStamp> findByImei(String imei) {
        boolean connection = service.openConnection();
        List<LocationStamp> locationStamps = service.findByImei(imei);
        service.closeConnectionIf(connection);
        return locationStamps;
    }

    @Override
    public BaseResource getResource() {
        return service.getResource();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

    @Override
    public void setContext(Context context) {
        service.setContext(context);
    }
}
