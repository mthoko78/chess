package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.RecordedCallService;

@RestController
@RequestMapping("recorded-call")
public class RecordedCallController extends BaseController<RecordedCall> {

	@Autowired
	private RecordedCallService service;

	@Override
	public BaseService<RecordedCall> getService() {
		return service;
	}

	@PutMapping("by-caller-simno-imei-exclude-ids")
	List<RecordedCall> findByCallerOrSimNoOrImeiExcludingIds(@RequestBody List<Long> ids,
			@RequestParam("caller") String caller, @RequestParam("simNo") String simNo,
			@RequestParam("imei") String imei) {
		return service.findByCallerOrSimNoOrImeiExcludingIds(ids, caller, simNo, imei);
	}

}
