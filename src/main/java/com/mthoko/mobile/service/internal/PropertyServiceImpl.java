package com.mthoko.mobile.service.internal;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.PropertyResourceRemote;
import com.mthoko.mobile.service.PropertyService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class PropertyServiceImpl extends BaseServiceImpl<Property> implements PropertyService {

    private final PropertyResourceRemote propertyResourceRemote;

	public PropertyServiceImpl() {
        propertyResourceRemote = new PropertyResourceRemote(new ConnectionWrapper(null));
    }

    @Override
    public BaseResourceRemote getResource() {
        return propertyResourceRemote;
    }

}
