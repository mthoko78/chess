package com.mthoko.learners.domain.mail;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class SimpleMail extends UniqueEntity {

	private String sender;

	private String recipient;

	private String subject;

	private String body;

	private Date dateSent;

	private Date dateDelivered;

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public boolean isDelivered() {
		return dateDelivered != null;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTo() {
		return recipient;
	}

	public void setTo(String to) {
		this.recipient = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public Date getDateDelivered() {
		return dateDelivered;
	}

	public void setDateDelivered(Date dateDelivered) {
		this.dateDelivered = dateDelivered;
	}

	@Override
	public String getUniqueIdentifier() {
		return sender + "|" + recipient + "|" + dateSent;
	}

}
