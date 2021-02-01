package com.mthoko.mobile.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;

@RestController
@RequestMapping("account")
public class AccountController extends BaseController<Account> {

	@Autowired
	private AccountService service;

	@Override
	public BaseService<Account> getService() {
		return service;
	}

	@RequestMapping("email/{email}")
	public Account findByEmail(@PathVariable("email") String email) {
		return service.findByEmail(email);
	}

	@PostMapping("match")
	public List<Account> findMatchingAccounts(@RequestBody Account account) {
		return service.findMatchingAccounts(account);
	}

	@GetMapping("phone/{phone}")
	public Account findByPhone(@PathVariable("phone") String phone) {
		return service.findByPhone(phone);
	}

}
