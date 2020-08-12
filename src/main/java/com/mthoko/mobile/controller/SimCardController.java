package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("sim-card")
public class SimCardController extends BaseController<SimCard> {

	private final SimCardService service = ServiceFactory.getSimCardService();
	
	@Override
	public BaseService getService() {
		return service;
	}

	@GetMapping("/simNo/{simNo}")
	@ResponseBody
	public SimCard findBySimNo(@PathVariable("simNo") String simNo) {
		return service.findBySimNo(simNo);
	}

	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody SimCard simCard) {
		return super.save(simCard);
	}

	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<SimCard> simCard) {
		return super.saveAll(simCard);
	}

}
