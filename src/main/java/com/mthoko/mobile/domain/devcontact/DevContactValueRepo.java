package com.mthoko.mobile.domain.devcontact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevContactValueRepo extends JpaRepository<DevContactValue, Long> {
}