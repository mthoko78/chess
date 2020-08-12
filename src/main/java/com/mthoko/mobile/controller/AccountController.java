package com.mthoko.mobile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("account")
public class AccountController extends BaseController<Account> {

	private final AccountService service = ServiceFactory.getAccountService();

	@Override
	public BaseService getService() {
		return service;
	}

	@RequestMapping("/{email}")
	@ResponseBody
	public Account findByEmail(@PathVariable("email") String email) {
		return service.findByEmail(email);
	}

}
