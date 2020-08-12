package com.mthoko.mobile.service.internal;

import java.util.List;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.resource.SmsResourceRemote;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.common.MailService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class SmsServiceImpl extends BaseServiceImpl implements SmsService {

	private final SmsResourceRemote resource;
	private final MailService mailService;

	public SmsServiceImpl() {
		resource = new SmsResourceRemote(new ConnectionWrapper(null));
		mailService = new MailService();
	}

	@Override
	public void sendAsMail(Sms sms) {
		mailService.sendEmail("New Sms - " + sms.getId(), sms.getFormattedString());
	}

	public void sendAsMail(List<Sms> smsList) {
		if (smsList == null || smsList.isEmpty()) {
			return;
		}
		if (smsList.size() == 1) {
			sendAsMail(smsList.get(0));
		}
		mailService.sendEmail("New Smses", getBodyText(smsList));
	}

	public String getBodyText(List<Sms> smsList) {
		StringBuilder body = new StringBuilder();
		for (Sms sms : smsList) {
			body.append(sms.getFormattedString());
		}
		String text = body.toString();
		return text;
	}

	public void saveAllToRemote(List<Sms> unverified) {
		resource.saveAll(unverified);
	}

	@Override
	public List<Sms> findByRecipient(String recipient) {
		return resource.findByRecipient(recipient);
	}

	@Override
	public BaseResourceRemote<Sms> getResource() {
		return resource;
	}

	@Override
	public int countByRecipient(String recipient) {
		return resource.countSmsesByRecipient(recipient);
	}

	@Override
	public List<Sms> findByRecipientExcludingIds(List<Long> ids, String recipient) {
		return resource.findByRecipientWithIdsNotIn(ids, recipient);
	}

}
