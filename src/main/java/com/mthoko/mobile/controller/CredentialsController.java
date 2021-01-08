package com.mthoko.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.CredentialsService;

@RestController
@RequestMapping("credentials")
public class CredentialsController extends BaseController<Credentials> {

	@Autowired
	private CredentialsService service;

	@Override
	public BaseService<Credentials> getService() {
		return this.service;
	}

	@GetMapping("memberId/{memberId}")
	public Credentials findByMemberId(@PathVariable("memberId") Long memberId) {
		return service.findByMemberId(memberId);
	}

}
