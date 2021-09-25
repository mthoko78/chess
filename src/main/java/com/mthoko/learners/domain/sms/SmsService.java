package com.mthoko.learners.domain.sms;

import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.account.PhoneVerification;
import com.mthoko.learners.domain.mail.SimpleMail;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SmsService extends BaseService<Sms> {

	List<Sms> findByRecipient(String recipient);

	SimpleMail sendAsMail(Sms sms);

	SimpleMail sendAllAsMail(List<Sms> sms);

	int countByRecipient(String recipient);

	List<Sms> findByRecipientExcludingIds(String recipient, List<Long> ids);

	MessageResponse sendSms(Sms sms);

    MessageResponse forwardSms(Sms sms, String to);

	MessageResponse saveMessageResponse(MessageResponse response);

	Object deleteByRecipientIn(List<String> phones);

	Map<String, Long> verificationForMailedSmsesByRecipient(String recipient);

    List<SmsDeliveryReport> findAllSmsDeliveryReports();

	SmsDeliveryReport saveSmsDeliveryReport(SmsDeliveryReport report);

	List<SmsDeliveryReport> findSmsDeliveryReportsByMessageId(String messageId);

	List<SmsDeliveryReport> findSmsDeliveryReportsByRecipient(String recipient);

	SmsDeliveryReport createSmsDeliveryReport(Map<String, Object> deliveryReport);

	Optional<Sms> findByMessageId(String messageId);

    int countByRecipientImei(String recipientImei);

	List<Sms> findByRecipientImei(String recipientImei);

    Sms sendPhoneVerificationSms(String phoneNumber, PhoneVerification verification);
}
