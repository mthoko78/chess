package com.mthoko.mobile.domain.locationstamp;

import com.mthoko.mobile.common.controller.BaseController;
import com.mthoko.mobile.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("location-stamp")
public class LocationStampController extends BaseController<LocationStamp> {

	@Autowired
	private LocationStampService service;

	@Override
	public BaseService<LocationStamp> getService() {
		return service;
	}

	@RequestMapping("{imei}")
	public List<LocationStamp> findByImei(@PathVariable("imei") String imei) {
		return service.findByImei(imei);
	}

	@RequestMapping("recent/{imei}")
	public Optional<LocationStamp> findMostRecentByImei(@PathVariable("imei") String imei) {
		return service.findMostRecentByImei(imei);
	}

	@GetMapping("count/imei/{imei}")
	public Integer countByImei(@PathVariable("imei") String imei) {
		return service.countByImei(imei);
	}

}
