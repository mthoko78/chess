package com.mthoko.mobile.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mthoko.mobile.entity.Address;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
