package com.mthoko.mobile.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.repo.AddressRepo;
import com.mthoko.mobile.service.AddressService;

@Service
public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {

	@Autowired
	private AddressRepo addressRepo;

	@Override
	public JpaRepository<Address, Long> getRepo() {
		return addressRepo;
	}

}
