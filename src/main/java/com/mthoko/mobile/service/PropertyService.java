package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.Property;

public interface PropertyService extends BaseService<Property> {

	Property findByKey(String key);

	String unsetProperty(String key);
}
