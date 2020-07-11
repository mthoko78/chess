package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.util.DatabaseWrapper;

public class DevContactValueResource extends BaseResource<DevContactValue> {

    public DevContactValueResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, DevContactValue.class, databaseWrapper);
    }

}