package com.mthoko.mobile.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.util.DataManager;

@Controller
public class HomeController {

	@Autowired
	private LocationStampService service;

	@Autowired
	DevContactService devContactService;

	@Autowired
	private SimContactService simContactService;

	@RequestMapping("address/{imei}")
	public String addressByImeiFromLatlng(Map<String, Object> model, @PathVariable("imei") String imei) {
		try {
			ArrayList<String> output = new ArrayList<String>();
			LocationStamp locationStamp = service.findMostRecentByImei(imei);
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

	public String getGoogleMapsLink(Double latitude, Double longitude) {
		return String.format("https://www.google.co.za/maps/search/%s,%s", latitude, longitude);
	}

	@RequestMapping("latlng")
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

	@RequestMapping
	public String index() {
		return "index";
	}

	@RequestMapping("time")
	@ResponseBody
	public Date getTime() {
		long twoHours = 2 * 60 * 60 * 1000;
		return new Date(new Date().getTime() + twoHours);
	}

	@RequestMapping("app/property/name/{name}")
	@ResponseBody
	public String getAppProperty(@PathVariable("name") String name) {
		return service.getAppProperty(name);
	}

	@GetMapping("device-contacts/imei/{imei}")
	public String deviceContacts(@PathVariable("imei") String imei, Map<String, Object> model) {
		List<DevContact> devContacts = devContactService.findByImei(imei);
		model.put("contacts", devContacts);
		return "device-contacts";
	}

	@GetMapping("sim-contacts/simNo/{simNo}")
	public String simContacts(@PathVariable("simNo") String simNo, Map<String, Object> model) {
		List<SimContact> devContacts = simContactService.findBySimNo(simNo);
		model.put("contacts", devContacts);
		return "sim-contacts";
	}

}
