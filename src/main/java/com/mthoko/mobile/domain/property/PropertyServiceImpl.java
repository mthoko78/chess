package com.mthoko.mobile.domain.property;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertyServiceImpl extends BaseServiceImpl<Property> implements PropertyService {

	@Autowired
	private PropertyRepo propertyRepo;

	@Override
	public JpaRepository<Property, Long> getRepo() {
		return propertyRepo;
	}

	@Override
	public Optional<Property> findByKey(String key) {
		return propertyRepo.findByPropertyKey(key);
	}

}
