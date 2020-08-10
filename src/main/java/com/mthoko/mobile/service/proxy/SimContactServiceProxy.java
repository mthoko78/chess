package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SimContactServiceImpl;

public class SimContactServiceProxy extends BaseServiceImpl implements SimContactService {

	private final SimContactServiceImpl service;

	public SimContactServiceProxy() {
		service = new SimContactServiceImpl();
	}

	@Override
	public BaseResourceRemote<SimContact> getResource() {
		return service.getResource();
	}

}