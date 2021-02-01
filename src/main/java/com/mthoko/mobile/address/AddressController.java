package com.mthoko.mobile.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;

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
