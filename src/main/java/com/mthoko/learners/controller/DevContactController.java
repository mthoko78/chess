package com.mthoko.learners.controller;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.service.BaseService;
import com.mthoko.learners.persistence.entity.DevContact;
import com.mthoko.learners.service.DevContactService;
import com.mthoko.learners.persistence.entity.DevContactValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@PostMapping("optimize")
	public List<DevContact> optimizeByImei(@RequestBody List<DevContact> contacts) {
		return service.optimize(contacts);
	}

	@PostMapping("duplicates")
	public List<DevContact> extractDuplicates(@RequestBody List<DevContact> contacts) {
		return service.extractDuplicates(contacts);
	}

	@PostMapping("duplicate-values")
	public List<DevContactValue> extractDuplicateValues(@RequestBody List<DevContact> contacts) {
		return service.extractDuplicateValues(contacts);
	}

	@DeleteMapping("delete-values")
	public ResponseEntity<Object> deleteValues(@RequestBody List<DevContactValue> values) {
		service.deleteValues(values);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("empty-values")
	public List<DevContact> extractContactsWithEmptyValues(@RequestBody List<DevContact> contacts) {
		return service.extractContactsWithEmptyValues(contacts);
	}

}
