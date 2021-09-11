package com.mthoko.mobile.domain.sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsResourceRepo extends JpaRepository<Sms, Long> {

    int countByRecipient(String recipient);

    List<Sms> findByRecipientAndIdNotIn(String recipient, List<Long> ids);

    List<Sms> findByRecipient(String recipient);

    Object deleteByRecipient(String recipient);

    Object deleteByRecipientIn(List<String> phones);

    Optional<Sms> findByMessageId(String messageId);

    int countByRecipientImei(String recipientImei);

    List<Sms> findByRecipientImei(String recipientImei);
}
