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

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SimContactService;

@RestController
@RequestMapping("sim-contact")
public class SimContactController extends BaseController<SimContact> {

	@Autowired
	private SimContactService service;

	@Override
	public BaseService<SimContact> getService() {
		return service;
	}

	@GetMapping("count/simNo/{simNo}")
	public Integer countBySimNo(@PathVariable("simNo") String simNo) {
		return service.countBySimNo(simNo);
	}

	@GetMapping("simCardId/{simCardId}")
	public List<SimContact> findBySimCardId(@PathVariable("simCardId") Long simCardId) {
		return service.findBySimCardId(simCardId);
	}

	@GetMapping("simNo/{simNo}")
	public List<SimContact> findBySimNo(@PathVariable("simNo") String simNo) {
		return service.findBySimNo(simNo);
	}

	@PostMapping("excluding-ids/simNo/{simNo}")
	public List<SimContact> findBySimNoExcludingIds(@RequestBody List<Long> ids, @PathVariable("simNo") String simNo) {
		return service.findBySimNoExcludingIds(ids, simNo);
	}

	@DeleteMapping("optimize/simNo/{simNo}")
	public List<SimContact> optimizeByImei(@PathVariable("simNo") String simNo) {
		return service.optimizeBySimNo(simNo);
	}

}
