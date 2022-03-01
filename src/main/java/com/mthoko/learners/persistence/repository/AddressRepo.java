package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
