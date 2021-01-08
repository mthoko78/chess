package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.DeviceService;

@RestController
@RequestMapping("device")
public class DeviceController extends BaseController<Device> {

	@Autowired
	private DeviceService service;

	@Override
	public BaseService<Device> getService() {
		return service;
	}

	@GetMapping("memberId/{memberId}")
	public List<Device> findByMemberId(@PathVariable("memberId") Long memberId) {
		return service.findByMemberId(memberId);
	}

	@GetMapping("imei/{imei}")
	public Device findByImei(@PathVariable("imei") String imei) {
		return service.findByImei(imei);
	}
}
