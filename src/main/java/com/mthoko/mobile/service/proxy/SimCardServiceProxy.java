package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SimCardServiceImpl;

public class SimCardServiceProxy extends BaseServiceImpl implements SimCardService {

	private final SimCardServiceImpl service;

	public SimCardServiceProxy() {
		service = new SimCardServiceImpl();
	}

	@Override
	public BaseResourceRemote<SimCard> getResource() {
		return service.getResource();
	}

}
