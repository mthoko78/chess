package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.Sms;

public interface SmsService extends BaseService<Sms> {

    void setProperty(String key, String value);
    
    void saveAllToRemote(List<Sms> smses);

	List<Sms> findByRecipient(String recipient);

	void sendAsMail(Sms sms);
}
