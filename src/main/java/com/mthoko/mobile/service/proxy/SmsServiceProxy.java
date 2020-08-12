package com.mthoko.mobile.service.proxy;

import java.util.List;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SmsServiceImpl;

public class SmsServiceProxy extends BaseServiceImpl implements SmsService {

	private final SmsServiceImpl service;

	public SmsServiceProxy() {
		service = new SmsServiceImpl();
	}

	@Override
	public List<Sms> findByRecipient(String recipient) {
		boolean remoteConnection = service.openConnection();
		List<Sms> smses = service.findByRecipient(recipient);
		service.closeConnectionIf(remoteConnection);
		return smses;
	}

	@Override
	public void sendAsMail(Sms sms) {
		service.sendAsMail(sms);
	}

	@Override
	public BaseResourceRemote<Sms> getResource() {
		return service.getResource();
	}

	@Override
	public int countByRecipient(String recipient) {
		boolean openConnection = service.openConnection();
		int count = service.countByRecipient(recipient);
		service.closeConnectionIf(openConnection);
		return count;
	}

	@Override
	public List<Sms> findByRecipientExcludingIds(List<Long> ids, String recipient) {
		boolean openConnection = service.openConnection();
		List<Sms> smses = service.findByRecipientExcludingIds(ids, recipient);
		service.closeConnectionIf(openConnection);
		return smses;
	}
}
