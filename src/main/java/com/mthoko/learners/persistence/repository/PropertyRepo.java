package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepo extends JpaRepository<Property, Long> {

	Optional<Property> findByPropertyKey(String key);

}
