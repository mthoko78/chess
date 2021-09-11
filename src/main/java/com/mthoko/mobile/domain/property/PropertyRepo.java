package com.mthoko.mobile.domain.property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepo extends JpaRepository<Property, Long> {

	public Optional<Property> findByPropertyKey(String key);

}
