package com.mthoko.mobile.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.util.DataManager;

@Controller
@RequestMapping("location-stamp")
public class LocationStampController extends BaseController<LocationStamp> {

	private final LocationStampService service = ServiceFactory.getLocationStampService();

	@Override
	public BaseService getService() {
		return service;
	}

	@RequestMapping("/address/{imei}")
	public String addressByImeiFromLatlng(Map<String, Object> model, @PathVariable("imei") String imei) {
		try {
			ArrayList<String> output = new ArrayList<String>();
			LocationStamp locationStamp = findMostRecentByImei(imei);
			if (locationStamp != null) {
				double latitude = Double.parseDouble(locationStamp.getLatitude());
				double longitude = Double.parseDouble(locationStamp.getLongitude());
				Address address = DataManager.retrieveAddress(latitude, longitude);
				output.add("Device: " + locationStamp.getDeviceRequested());
				output.add("Imei: " + locationStamp.getImei());
				Date timeCaptured = locationStamp.getTimeCaptured();
				output.add("Time: " + timeCaptured);
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
			}
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	@RequestMapping("/{imei}")
	@ResponseBody
	public List<LocationStamp> findByImei(@PathVariable("imei") String imei) {
		return service.findByImei(imei);
	}

	@RequestMapping("/recent/{imei}")
	@ResponseBody
	public LocationStamp findMostRecentByImei(String imei) {
		return service.findMostRecentByImei(imei);
	}

	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody LocationStamp locationStamp) {
		return super.save(locationStamp);
	}

	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<LocationStamp> locationStamps) {
		return super.saveAll(locationStamps);
	}

}
