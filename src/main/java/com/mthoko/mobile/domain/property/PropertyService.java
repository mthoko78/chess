package com.mthoko.mobile.domain.property;

import com.mthoko.mobile.common.service.BaseService;

import java.util.Optional;

public interface PropertyService extends BaseService<Property> {

	Optional<Property> findByKey(String key);

	String unsetProperty(String key);
}
