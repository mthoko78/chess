package com.mthoko.mobile.domain.sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsDeliveryReportRepo extends JpaRepository<SmsDeliveryReport, Long> {

	public int countByRecipient(String recipient);

	public List<SmsDeliveryReport> findByMessageId(String messageId);

	public List<SmsDeliveryReport> findByRecipient(String recipient);

}
