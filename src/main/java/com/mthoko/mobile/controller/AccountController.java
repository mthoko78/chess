package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping("/email/{email}")
	@ResponseBody
	public Account findByEmail(@PathVariable("email") String email) {
		return service.findByEmail(email);
	}
	
	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody Account account) {
		return super.save(account);
	}
	
	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<Account> accounts) {
		return super.saveAll(accounts);
	}

}
