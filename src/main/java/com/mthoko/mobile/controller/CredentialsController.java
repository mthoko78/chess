package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.CredentialsService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("credentials")
public class CredentialsController extends BaseController<Credentials> {

	private final CredentialsService service = ServiceFactory.getCredentialsService();

	@Override
	public BaseService getService() {
		return this.service;
	}

	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody Credentials credentials) {
		return super.save(credentials);
	}

	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<Credentials> credentials) {
		return super.saveAll(credentials);
	}

}
