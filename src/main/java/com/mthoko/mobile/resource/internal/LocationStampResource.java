package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.List;

public class LocationStampResource extends BaseResource<LocationStamp> {

    public LocationStampResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, LocationStamp.class, databaseWrapper);
    }

    public List<LocationStamp> findByImei(String imei) {
        return findWhere(String.format(getEntityName() + ".imei = '%s'", imei));
    }
}