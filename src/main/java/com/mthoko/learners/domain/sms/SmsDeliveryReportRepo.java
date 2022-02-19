package com.mthoko.learners.domain.sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsDeliveryReportRepo extends JpaRepository<SmsDeliveryReport, Long> {

    int countByRecipient(String recipient);

    List<SmsDeliveryReport> findByMessageId(String messageId);

    List<SmsDeliveryReport> findByRecipient(String recipient);

    Optional<SmsDeliveryReport> findByMessageIdAndRequestId(String messageId, String requestId);

    boolean existsByMessageIdAndRequestId(String messageId, String requestId);
}
