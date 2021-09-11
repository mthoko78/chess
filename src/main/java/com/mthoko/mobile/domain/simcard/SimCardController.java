package com.mthoko.mobile.domain.simcard;

import com.mthoko.mobile.common.controller.BaseController;
import com.mthoko.mobile.common.service.BaseService;
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
