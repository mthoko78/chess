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

    public List<LocationStamp> findByImei(String imei) {
        boolean connection = service.openConnection();
        List<LocationStamp> device = service.findByImei(imei);
        service.closeConnectionIf(connection);
        return device;
    }

    @Override
    public List<Long> saveToRemote(List<LocationStamp> locationStamps) {
        boolean openConnection = service.openConnection();
        boolean transaction = service.beginTransaction();
        List<Long> ids = service.saveToRemote(locationStamps);
        service.endTransactionIf(transaction);
        service.closeConnectionIf(openConnection);
        return ids;
    }

    @Override
    public BaseResourceRemote getResource() {
        return service.getResource();
    }

}
