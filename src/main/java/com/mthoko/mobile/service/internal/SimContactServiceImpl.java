package com.mthoko.mobile.service.internal;

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.SimContactResourceRemote;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class SimContactServiceImpl extends BaseServiceImpl<SimContact> implements SimContactService {

	private final SimContactResourceRemote simContactResourceRemote;

	public SimContactServiceImpl() {
		simContactResourceRemote = new SimContactResourceRemote(new ConnectionWrapper(null));
	}

	@Override
	public BaseResourceRemote<SimContact> getResource() {
		return simContactResourceRemote;
	}
}
