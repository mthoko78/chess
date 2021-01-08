package com.mthoko.mobile.service.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.SimpleMail;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.repo.MailRepository;
import com.mthoko.mobile.service.internal.BaseServiceImpl;

@Service
public class MailService extends BaseServiceImpl<SimpleMail> {

	private static final String TO = "mthoko78@outlook.com";

	private static final Long SUCCESS = 0l;

	public static final String MAIL_PROPERTIES = "mail.properties";

	private static final String MAIL_USERNAME = "mail.username";

	private static final String MAIL_PASSWORD = "mail.password";

	private final MailRepository mailRepository;
	
	@Autowired
	public MailService(MailRepository mailRepository) {
		this.mailRepository = mailRepository;
	}

	public List<Long> sendEmail(String subject, String text) {
		return sendMail(TO, subject, text);
	}

	public List<Long> sendMail(String to, String subject, String text) {
		try {
			Properties mailProperties = getMailProperties();
			MimeMessage message = new MimeMessage(getMailSession(mailProperties));
			message.setFrom(new InternetAddress(mailProperties.getProperty(MAIL_USERNAME)));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
			return Arrays.asList(SUCCESS);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	private Session getMailSession(Properties mailProperties) {
		final String user = mailProperties.getProperty(MAIL_USERNAME);
		final String password = mailProperties.getProperty(MAIL_PASSWORD);
		Properties props = System.getProperties();
		props.putAll(mailProperties);
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		return session;
	}

	public Properties getMailProperties() {
		InputStream resource = getClass().getClassLoader().getResourceAsStream(MAIL_PROPERTIES);
		Properties properties = new Properties();
		try {
			properties.load(resource);
			return properties;
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public JpaRepository<SimpleMail, Long> getRepo() {
		return mailRepository;
	}

	public void sendEmail(SimpleMail mail) {
		save(mail);
		sendMail(TO, mail.getSubject(), mail.getBody());
		mail.setDateDelivered(new Date());
		update(mail);
	}
}