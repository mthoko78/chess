package com.mthoko.mobile.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.repo.PropertyRepo;
import com.mthoko.mobile.service.PropertyService;

@Service
public class PropertyServiceImpl extends BaseServiceImpl<Property> implements PropertyService {

	@Autowired
	private PropertyRepo propertyRepo;

	@Override
	public JpaRepository<Property, Long> getRepo() {
		return propertyRepo;
	}

	@Override
	public Property findByKey(String key) {
		return propertyRepo.findByPropertyKey(key);
	}

}
