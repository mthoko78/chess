package com.mthoko.learners.domain.mail;

import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.exception.ApplicationException;
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
public class MailService extends BaseServiceImpl<com.mthoko.learners.domain.mail.SimpleMail> {

    private static final String TO = "mthoko78@outlook.com";

    public static final int SUCCESS = 1;

    public static final String MAIL_PROPERTIES = "mail.properties";

    private static final String MAIL_USERNAME = "mail.username";

    private static final String MAIL_PASSWORD = "mail.password";

    private final com.mthoko.learners.domain.mail.MailRepository mailRepository;

    @Autowired
    public MailService(com.mthoko.learners.domain.mail.MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    public com.mthoko.learners.domain.mail.SimpleMail sendEmail(String subject, String text) {
        com.mthoko.learners.domain.mail.SimpleMail mail = new com.mthoko.learners.domain.mail.SimpleMail();
        mail.setRecipient(TO);
        mail.setSubject(subject);
        mail.setBody(text);
        return sendMail(mail);
    }

    public com.mthoko.learners.domain.mail.SimpleMail sendMail(com.mthoko.learners.domain.mail.SimpleMail mail) {
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

    private MimeMessage getMimeMessage(com.mthoko.learners.domain.mail.SimpleMail mail) throws MessagingException {
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
    public JpaRepository<com.mthoko.learners.domain.mail.SimpleMail, Long> getRepo() {
        return mailRepository;
    }
}