package com.mthoko.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SimCardService;

@RestController
@RequestMapping("sim-card")
public class SimCardController extends BaseController<SimCard> {

	@Autowired
	private SimCardService service;

	@Override
	public BaseService<SimCard> getService() {
		return service;
	}

	@GetMapping("simNo/{simNo}")
	public SimCard findBySimNo(@PathVariable("simNo") String simNo) {
		return service.findBySimNo(simNo);
	}

}
