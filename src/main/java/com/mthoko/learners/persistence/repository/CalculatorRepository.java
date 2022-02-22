package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.CalculatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalculatorRepository extends JpaRepository<CalculatorEntity, UUID> {
}
