package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.Sms;

public interface SmsService extends BaseService {

    void setProperty(String key, String value);

	List<Sms> findByRecipient(String recipient);

	void sendAsMail(Sms sms);

	int countByRecipient(String recipient);

	List<Sms> findByRecipientExcludingIds(List<Long> ids, String recipient);
}
