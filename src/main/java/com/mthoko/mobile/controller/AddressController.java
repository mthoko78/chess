package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.service.proxy.AddressServiceProxy;

@Controller
@RequestMapping("address")
public class AddressController extends BaseController<Address> {

	private final AddressServiceProxy service = ServiceFactory.getAddressService();

	@Override
	public BaseService getService() {
		return service;
	}

	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody Address address) {
		return super.save(address);
	}

	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<Address> addresses) {
		return super.saveAll(addresses);
	}

}
