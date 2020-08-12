package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("sim-contact")
public class SimContactController extends BaseController<SimContact> {

	private final SimContactService service = ServiceFactory.getSimContactService();
	
	@Override
	public BaseService getService() {
		return service;
	}
	
    @GetMapping("/count/simNo/{simNo}")
    public Integer countBySimNo(@PathVariable("simNo") String simNo) {
    	return service.countBySimNo(simNo);
    }

    @GetMapping("/simCardId/{simCardId}")
    public List<SimContact> findBySimCardId(@PathVariable("simCardId") Long simCardId) {
    	return service.findBySimCardId(simCardId);
    }

    @GetMapping("/simNo/{simNo}")
    public List<SimContact> findBySimNo(@PathVariable("simNo") String simNo) {
    	return service.findBySimNo(simNo);
    }

    @GetMapping("excluding-ids/simNo/{simNo}")
    public List<SimContact> findBySimNoExcludingIds(@RequestBody List<Long> ids, @PathVariable("simNo") String simNo) {
    	return service.findBySimNoExcludingIds(ids, simNo);
    }

	
	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody SimContact simContact) {
		return super.save(simContact);
	}
	
	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<SimContact> simContacts) {
		return super.saveAll(simContacts);
	}

}
