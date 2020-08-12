package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.RecordedCallServiceImpl;

public class RecordedCallServiceProxy extends BaseServiceImpl implements RecordedCallService {

	private final RecordedCallServiceImpl service;

	public RecordedCallServiceProxy() {
		service = new RecordedCallServiceImpl();
	}

	@Override
	public BaseResourceRemote<RecordedCall> getResource() {
		return service.getResource();
	}

}
