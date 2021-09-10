package com.mthoko.mobile.domain.sms;

import com.mthoko.mobile.common.BaseServiceImpl;
import com.mthoko.mobile.common.MailService;
import com.mthoko.mobile.domain.property.Property;
import com.mthoko.mobile.domain.property.PropertyService;
import com.mthoko.mobile.exception.ApplicationException;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmsServiceImpl extends BaseServiceImpl<Sms> implements SmsService {

	@Autowired
	private SmsResourceRepo smsResourceRepo;

	@Autowired
	private MailService mailService;

	@Autowired
	private PropertyService propertyResource;

	@Override
	public Object sendAsMail(Sms sms) {
		try {
			return mailService.sendEmail("New Sms - " + sms.getId(), sms.getFormattedString());
		} catch (Exception e) {
			String key = "Error:Sms:" + sms.getUniqueIdentifier();
			String value = "" + e.getMessage();
			Property property = new Property(key, value);
			propertyResource.save(property);
			return Arrays.asList(property.getId());
		}
	}

	@Override
	public Object sendAllAsMail(List<Sms> smsList) {
		if (smsList == null || smsList.isEmpty()) {
			return Arrays.asList(-1l);
		}
		if (smsList.size() == 1) {
			return sendAsMail(smsList.get(0));
		}
		try {
			return mailService.sendEmail("New Smses", getBodyText(smsList));
		} catch (Exception e) {
			String value = "" + e.getMessage();
			List<Property> properties = new ArrayList<>();
			for (Sms sms : smsList) {
				String key = "Error:Sms:" + sms.getUniqueIdentifier();
				properties.add(new Property(key, value));
			}
			propertyResource.saveAll(properties);
			return Arrays.asList(properties.get(0).getId());
		}
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
		smsResourceRepo.saveAll(unverified);
	}

	@Override
	public List<Sms> findByRecipient(String recipient) {
		return smsResourceRepo.findByRecipient(recipient);
	}

	@Override
	public JpaRepository<Sms, Long> getRepo() {
		return smsResourceRepo;
	}

	@Override
	public int countByRecipient(String recipient) {
		return smsResourceRepo.countByRecipient(recipient);
	}

	@Override
	public List<Sms> findByRecipientExcludingIds(String recipient, List<Long> ids) {
		return smsResourceRepo.findByRecipient(recipient).stream().filter(call -> !ids.contains(call.getId()) ).collect(Collectors.toList());
	}

	@Override
	public String sendSms(Sms sms, String to) {
		String apiSecret = getAppProperty("sms.apiSecret");
		String apiKey = getAppProperty("sms.apiKey");
		System.out.println(apiSecret + ":" + apiKey);
		NexmoClient client = new NexmoClient.Builder().apiKey(apiKey).apiSecret(apiSecret).build();
		String messageText = sms.getBody();
		if (to == null) {
			to = getAppProperty("sms.defaultRecipient");
		}
		TextMessage message = new TextMessage("Vonage APIs", to, messageText);
		String result = "";
		try {
			SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
			for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
				result += responseMessage + "\n";
			}
			return result;
		} catch (IOException | NexmoClientException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}

	@Override
	public Object deleteByRecipientIn(List<String> phones) {
		return smsResourceRepo.deleteByRecipientIn(phones);
	}

}
