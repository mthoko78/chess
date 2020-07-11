package com.mthoko.mobile.service.proxy;

import java.util.List;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SmsServiceImpl;

public class SmsServiceProxy extends BaseServiceImpl<Sms> implements SmsService {

	private final SmsServiceImpl service;

	public SmsServiceProxy() {
		service = new SmsServiceImpl();
	}

	@Override
	public BaseResourceRemote getRemoteResource() {
		return service.getRemoteResource();
	}

	@Override
	public void saveAllToRemote(List<Sms> smses) {
		boolean remoteConnection = service.openRemoteConnection();
		boolean remoteTransaction = service.beginRemoteTransaction();
		getRemoteResource().saveAll(smses);
		service.endRemoteTransactionIf(remoteTransaction);
		service.closeRemoteConnectionIf(remoteConnection);
	}
}
