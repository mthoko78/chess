package com.mthoko.mobile.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.Property;

@Repository
public interface PropertyRepo extends JpaRepository<Property, Long> {

	public Property findByPropertyKey(String key);

}
