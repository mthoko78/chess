package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("device")
public class DeviceController extends BaseController<Device> {

	private final DeviceService service = ServiceFactory.getDeviceService();

	@Override
	public BaseService getService() {
		return service;
	}

	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<Device> devices) {
		return super.saveAll(devices);
	}

	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody Device device) {
		return super.save(device);
	}
}
