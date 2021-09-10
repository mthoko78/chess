package com.mthoko.mobile.domain.property;

import com.mthoko.mobile.common.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
