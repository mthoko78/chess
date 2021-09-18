package com.mthoko.learners.domain.simcard;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sim-card")
public class SimCardController extends BaseController<SimCard> {

	@Autowired
	private SimCardService service;

	@Override
	public BaseService<SimCard> getService() {
		return service;
	}

}
