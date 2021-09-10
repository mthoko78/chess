package com.mthoko.mobile.domain.property;

import com.mthoko.mobile.common.BaseService;

public interface PropertyService extends BaseService<Property> {

	Property findByKey(String key);

	String unsetProperty(String key);
}
