package com.mthoko.mobile.service.common;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.mthoko.mobile.exception.ApplicationException;

public class MailService {

    public MailService() {
    }

    public void sendEmail(String subject, String text) {
        String to = "mthoko78@outlook.com";//change accordingly
        String from = "mthoko78@gmail.com";//change accordingly

        final String password = "Smj@dbn1";
        final String user = from;

        //Get the session object
        Properties props = System.getProperties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
//        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.transport.protocol", "smtp");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });


        //compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);

            // Send message
            Transport.send(message);
            System.out.println("message sent successfully....");
        } catch (MessagingException e) {
            throw new ApplicationException("Failed to send email", e);
        }
    }
}