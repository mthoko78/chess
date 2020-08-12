package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("location-stamp")
public class LocationStampController extends BaseController<LocationStamp> {

	private final LocationStampService service = ServiceFactory.getLocationStampService();

	@Override
	public BaseService getService() {
		return service;
	}

	@RequestMapping("/{imei}")
	@ResponseBody
	public List<LocationStamp> findByImei(@PathVariable("imei") String imei) {
		return service.findByImei(imei);
	}

	@RequestMapping("/recent/{imei}")
	@ResponseBody
	public LocationStamp findMostRecentByImei(@PathVariable("imei") String imei) {
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
