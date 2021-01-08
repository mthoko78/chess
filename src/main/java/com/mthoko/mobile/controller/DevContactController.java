package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.DevContactService;

@RestController
@RequestMapping("dev-contact")
public class DevContactController extends BaseController<DevContact> {

	@Autowired
	private DevContactService service;

	@Override
	public BaseService<DevContact> getService() {
		return service;
	}

	@GetMapping("imei/{imei}")
	public List<DevContact> findByImei(@PathVariable("imei") String imei) {
		return service.findByImei(imei);
	}

	@PostMapping("exclude-ids/imei/{imei}")
	public List<DevContact> findByImeiExcludingIds(@PathVariable("imei") String imei, @RequestBody List<Long> ids) {
		return service.findByImeiExcludingIds(imei, ids);
	}

	@GetMapping("count/imei/{imei}")
	public Integer countByImei(@PathVariable("imei") String imei) {
		return service.countByImei(imei);
	}

	@GetMapping("empty-values/imei/{imei}")
	public List<DevContact> findByImeiWithEmptyValues(@PathVariable("imei") String imei) {
		return service.findByImeiWithEmptyValues(imei);
	}

	@DeleteMapping("delete-with-empty-values/imei/{imei}")
	public List<DevContact> deleteWithEmptyValues(@PathVariable("imei") String imei) {
		return service.deleteWithEmptyValues(imei);
	}

	@GetMapping("redundant/imei/{imei}")
	public List<DevContact> findRedundantByImei(@PathVariable("imei") String imei) {
		return service.findRedundantByImei(imei);
	}

	@GetMapping("redundant-values/imei/{imei}")
	public List<DevContactValue> findRedundantValuesByImei(@PathVariable("imei") String imei) {
		return service.findRedundantValuesByImei(imei);
	}

	@DeleteMapping("delete-redundant-values/imei/{imei}")
	public List<DevContactValue> deleteRedundantValuesByImei(@PathVariable("imei") String imei) {
		return service.deleteRedundantValuesByImei(imei);
	}

	@DeleteMapping("optimize/imei/{imei}")
	public List<DevContact> optimizeByImei(@PathVariable("imei") String imei) {
		return service.optimizeByImei(imei, true);
	}

	@PostMapping("optimize/")
	public List<DevContact> optimizeByImei(@RequestBody List<DevContact> contacts) {
		return service.optimize(contacts);
	}

}
