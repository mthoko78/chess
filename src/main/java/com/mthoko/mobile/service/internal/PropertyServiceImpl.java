package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.PropertyResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.PropertyResourceRemote;
import com.mthoko.mobile.service.PropertyService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

public class PropertyServiceImpl extends BaseServiceImpl<Property> implements PropertyService {

    private final PropertyResource propertyResource;
    private final PropertyResourceRemote propertyResourceRemote;

    public PropertyServiceImpl(Context context) {
        propertyResource = new PropertyResource(context, new DatabaseWrapper());
        propertyResourceRemote = new PropertyResourceRemote(context, new ConnectionWrapper(null));
    }

    @Override
    public BaseResource getResource() {
        return propertyResource;
    }

    @Override
    public String getProperty(String key) {
        Property property = propertyResource.findByKey(key);
        String value = null;
        if (property != null) {
            value = property.getValue();
        }
        return value;
    }

    @Override
    public void setProperty(String key, String value) {
        getResource().setProperty(key, value);
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return propertyResourceRemote;
    }

    @Override
    public void setContext(Context context) {
        propertyResource.setContext(context);
    }
}
