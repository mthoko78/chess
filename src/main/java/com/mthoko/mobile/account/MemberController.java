package com.mthoko.mobile.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;

@RestController
@RequestMapping("member")
public class MemberController extends BaseController<Member> {

	@Autowired
	private MemberService service;

	@Override
	public BaseService<Member> getService() {
		return service;
	}

	@GetMapping("email/{email}")
	public Member findByEmail(@PathVariable("email") String email) {
		return service.findByEmail(email);
	}

	@GetMapping("phone/{phone}")
	public Member findByPhone(@PathVariable("phone") String phone) {
		return service.findByPhone(phone);
	}

}
