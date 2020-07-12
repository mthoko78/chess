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
	public BaseResourceRemote<Sms> getResource() {
		return service.getResource();
	}

	@Override
	public void saveAllToRemote(List<Sms> smses) {
		boolean remoteConnection = service.openConnection();
		boolean remoteTransaction = service.beginTransaction();
		getResource().saveAll(smses);
		service.endTransactionIf(remoteTransaction);
		service.closeConnectionIf(remoteConnection);
	}

	@Override
	public List<Sms> findByRecipient(String recipient) {
		boolean remoteConnection = service.openConnection();
		List<Sms> smses = service.findByRecipient(recipient);
		service.closeConnectionIf(remoteConnection);
		return smses;
	}
}
