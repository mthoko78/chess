package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.DevContactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevContactValueRepo extends JpaRepository<DevContactValue, Long> {
}