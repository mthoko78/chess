package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.util.DatabaseWrapper;

public class PropertyResource extends BaseResource<Property> {

    public PropertyResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, Property.class, databaseWrapper);
    }

    public Property findByKey(String key) {
        return findOneWhere(String.format("%s.key = '%s'", getEntityName(), key));
    }

}
