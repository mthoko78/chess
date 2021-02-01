package com.mthoko.mobile.sms;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsResourceRepo extends JpaRepository<Sms, Long> {

	public int countByRecipient(String recipient);

	public List<Sms> findByRecipientAndIdNotIn(String recipient, List<Long> ids);

	public List<Sms> findByRecipient(String recipient);

	public Object deleteByRecipient(String recipient);

	public Object deleteByRecipientIn(List<String> phones);

}
