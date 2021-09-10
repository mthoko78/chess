package com.mthoko.mobile.domain.account;

import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
