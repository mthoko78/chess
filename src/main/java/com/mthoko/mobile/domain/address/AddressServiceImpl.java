package com.mthoko.mobile.domain.address;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {

	@Autowired
	private AddressRepo addressRepo;

	@Override
	public JpaRepository<Address, Long> getRepo() {
		return addressRepo;
	}

}
