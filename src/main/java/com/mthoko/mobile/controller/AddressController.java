package com.mthoko.mobile.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.service.proxy.AddressServiceProxy;
import com.mthoko.mobile.util.DataManager;

@Controller
@RequestMapping("address")
public class AddressController extends BaseController<Address> {

	private final AddressServiceProxy service = ServiceFactory.getAddressService();

	@Override
	public BaseService getService() {
		return service;
	}

	@RequestMapping("/latlng")
	public String addressFromLatlng(Map<String, Object> model, @RequestParam("latitude") Double latitude,
			@RequestParam("longitude") Double longitude) {
		try {
			ArrayList<String> output = new ArrayList<String>();
			Address address = DataManager.retrieveAddress(latitude, longitude);
			output.add("Time: " + new Date(new Date().getTime() + 2 * (1000 * 60 * 60)));
			output.add("Country: " + address.getCountry());
			output.add("State: " + address.getState());
			output.add("City: " + address.getCity());
			output.add("PostalCode: " + address.getPostalCode());
			output.add("Street: " + address.getStreet());
			output.add("Latitude: " + latitude);
			output.add("Longitude: " + longitude);
			model.put("records", output);
			model.put("title", "Location Trace");
			model.put("link", getGoogleMapsLink(latitude, longitude));
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
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
