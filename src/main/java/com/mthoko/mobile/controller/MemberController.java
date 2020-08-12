package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.service.common.ServiceFactory;

@Controller
@RequestMapping("member")
public class MemberController extends BaseController<Member> {

	private final MemberService service = ServiceFactory.getMemberService();

	@Override
	public BaseService getService() {
		return service;
	}

	@PostMapping(SAVE)
	@ResponseBody
	public Long save(@RequestBody Member member) {
		return super.save(member);
	}

	@PostMapping(SAVE_ALL)
	@ResponseBody
	public List<Long> saveAll(@RequestBody List<Member> member) {
		return super.saveAll(member);
	}

}
