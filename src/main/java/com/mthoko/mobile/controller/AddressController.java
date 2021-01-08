package com.mthoko.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.service.AddressService;
import com.mthoko.mobile.service.BaseService;

@RestController
@RequestMapping("address")
public class AddressController extends BaseController<Address> {

	@Autowired
	private AddressService service;

	@Override
	public BaseService<Address> getService() {
		return service;
	}

}
