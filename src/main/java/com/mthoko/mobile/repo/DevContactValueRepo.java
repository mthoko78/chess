package com.mthoko.mobile.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.DevContactValue;

@Repository
public interface DevContactValueRepo extends JpaRepository<DevContactValue, Long> {
}