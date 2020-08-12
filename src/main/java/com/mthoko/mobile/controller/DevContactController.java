package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("dev-contact")
public class DevContactController extends BaseController<DevContact> {

	private final DevContactService service = ServiceFactory.getContactService();
	
	@Override
	public BaseService getService() {
		return service;
	}

	@RequestMapping("/exclude-ids/{imei}")
	@ResponseBody
	public List<DevContact> findByImeiExcludingIds(@RequestBody List<Long> ids,
			@PathVariable("imei") String imei) {
		return service.findByImeiExcludingIds(ids, imei);
	}

	@RequestMapping("/count-by-imei/{imei}")
	@ResponseBody
	public Integer countByImei(@PathVariable("imei") String imei) {
		return service.countByImei(imei);
	}

	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody DevContact devContact) {
		return super.save(devContact);
	}

	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<DevContact> devContacts) {
		return super.saveAll(devContacts);
	}

}
