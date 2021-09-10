package com.mthoko.mobile.domain.sms;

import com.mthoko.mobile.common.BaseService;

import java.util.List;

public interface SmsService extends BaseService<Sms> {

	List<Sms> findByRecipient(String recipient);

	Object sendAsMail(Sms sms);

	Object sendAllAsMail(List<Sms> sms);

	int countByRecipient(String recipient);

	List<Sms> findByRecipientExcludingIds(String recipient, List<Long> ids);

	String sendSms(Sms sms, String to);

	Object deleteByRecipientIn(List<String> phones);

}
