package com.mthoko.learners.domain.device;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
	public Optional<Device> findByImei(@PathVariable("imei") String imei) {
		return service.findByImei(imei);
	}
}
