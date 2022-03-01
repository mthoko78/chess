package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Property;

import java.util.Optional;

public interface PropertyService extends BaseService<Property> {

	Optional<Property> findByKey(String key);

	String unsetProperty(String key);
}
