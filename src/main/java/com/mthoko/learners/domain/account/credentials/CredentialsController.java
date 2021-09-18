package com.mthoko.learners.domain.account.credentials;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
	public Optional<Credentials> findByMemberId(@PathVariable("memberId") Long memberId) {
		return service.findByMemberId(memberId);
	}

}
