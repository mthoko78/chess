package com.mthoko.learners.service;

import com.mthoko.learners.exception.ApplicationException;
import com.mthoko.learners.persistence.entity.SimpleMail;
import com.mthoko.learners.persistence.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

@Service
public class MailService extends BaseServiceImpl<SimpleMail> {

    private static final String TO = "mthoko78@outlook.com";

    public static final int SUCCESS = 1;

    public static final String MAIL_PROPERTIES = "mail.properties";

    private static final String MAIL_USERNAME = "mail.username";

    private static final String MAIL_PASSWORD = "mail.password";

    private final MailRepository mailRepository;

    @Autowired
    public MailService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    public SimpleMail sendEmail(String subject, String text) {
        SimpleMail mail = new SimpleMail();
        mail.setRecipient(TO);
        mail.setSubject(subject);
        mail.setBody(text);
        return sendMail(mail);
    }

    public SimpleMail sendMail(SimpleMail mail) {
        try {
            save(mail);
            MimeMessage message = getMimeMessage(mail);
            Transport.send(message);
            InternetAddress sender = ((InternetAddress[]) message.getFrom())[0];
            String address = sender.getAddress();
            Date dateDelivered = new Date();
            mail.setSender(address);
            mail.setDateDelivered(dateDelivered);
            return update(mail);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    private MimeMessage getMimeMessage(SimpleMail mail) throws MessagingException {
        Properties mailProperties = getMailProperties();
        MimeMessage message = new MimeMessage(getMailSession(mailProperties));
        message.setFrom(new InternetAddress(mailProperties.getProperty(MAIL_USERNAME)));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getRecipient()));
        message.setSubject(mail.getSubject());
        message.setText(mail.getBody());
        return message;
    }

    private Session getMailSession(Properties mailProperties) {
        final String user = mailProperties.getProperty(MAIL_USERNAME);
        final String password = mailProperties.getProperty(MAIL_PASSWORD);
        Properties props = System.getProperties();
        props.putAll(mailProperties);
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        return session;
    }

    private Properties getMailProperties() {
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
}