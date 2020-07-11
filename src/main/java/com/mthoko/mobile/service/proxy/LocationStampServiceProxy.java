package com.mthoko.mobile.service.proxy;

import java.util.List;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.LocationStampServiceImpl;

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
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

}
