package com.mthoko.learners.domain.sms;

import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.common.util.HttpManager;
import com.mthoko.learners.common.util.RequestPackage;
import com.mthoko.learners.domain.account.PhoneVerification;
import com.mthoko.learners.domain.mail.MailService;
import com.mthoko.learners.domain.mail.SimpleMail;
import com.mthoko.learners.domain.property.Property;
import com.mthoko.learners.domain.property.PropertyService;
import com.mthoko.learners.exception.ApplicationException;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.learners.domain.sms.SmsController.DELIVERED_TO_GATEWAY;
import static com.mthoko.learners.domain.sms.SmsController.RECEIVED_BY_RECIPIENT;

@Service
public class SmsServiceImpl extends BaseServiceImpl<Sms> implements SmsService {

    private static final String SMS_DEFAULT_RECIPIENT = "sms.defaultRecipient";

    private static final String CLICKATEL_SMS_DEFAULT_RECIPIENT = "clickatel.sms.defaultRecipient";

    private static final String SMS_API_KEY = "sms.apiKey";

    private static final String CLICKATEL_SMS_API_KEY = "clickatel.sms.apiKey";

    private static final String CLICKATEL_URL_KEY = "clickatel.sms.url";

    private static final String SMS_API_SECRET = "sms.apiSecret";

    private static final String SMS_CLIENT_KEY = "sms.client";

    private static final String CLICKATEL = "clickatel";

    public static final String API_PHONE_NUMBER = "0820072381";

    private SmsResourceRepo smsResourceRepo;

    private final MailService mailService;

    private final PropertyService propertyResource;

    private final SmsDeliveryReportRepo smsDeliveryReportRepo;

    private final MessageResponseRepo messageResponseRepo;

    private final MessageRepo messageRepo;

    @Autowired
    public SmsServiceImpl(SmsResourceRepo smsResourceRepo, MailService mailService, PropertyService propertyResource, SmsDeliveryReportRepo smsDeliveryReportRepo, MessageResponseRepo messageResponseRepo, MessageRepo messageRepo) {
        this.smsResourceRepo = smsResourceRepo;
        this.mailService = mailService;
        this.propertyResource = propertyResource;
        this.smsDeliveryReportRepo = smsDeliveryReportRepo;
        this.messageResponseRepo = messageResponseRepo;
        this.messageRepo = messageRepo;
    }

    @Override
    public SimpleMail sendAsMail(Sms sms) {
        try {
            return mailService.sendEmail("New Sms - " + sms.getId(), sms.getFormattedString());
        } catch (Exception e) {
            String key = "Error:Sms:" + sms.getUniqueIdentifier();
            String value = "" + e.getMessage();
            Property property = new Property(key, value);
            propertyResource.save(property);
            throw new ApplicationException(property.getPropertyValue());
        }
    }

    @Override
    public SimpleMail sendAllAsMail(List<Sms> smsList) {
        if (smsList == null || smsList.isEmpty()) {
            return null;
        }
        if (smsList.size() == 1) {
            return sendAsMail(smsList.get(0));
        }
        try {
            return mailService.sendEmail("New Smses", getBodyText(smsList));
        } catch (Exception e) {
            String value = "" + e.getMessage();
            List<Property> properties = new ArrayList<>();
            for (Sms sms : smsList) {
                String key = "Error:Sms:" + sms.getUniqueIdentifier();
                properties.add(new Property(key, value));
            }
            propertyResource.saveAll(properties);
            throw new ApplicationException(properties.toString());
        }
    }

    private String getBodyText(List<Sms> smsList) {
        StringBuilder body = new StringBuilder();
        for (Sms sms : smsList) {
            body.append(sms.getFormattedString());
        }
        return body.toString();
    }

    @Override
    public List<Sms> findByRecipient(String recipient) {
        return smsResourceRepo.findByRecipient(recipient);
    }

    @Override
    public List<Sms> findByRecipientImei(String recipientImei) {
        return smsResourceRepo.findByRecipientImei(recipientImei);
    }

    @Override
    public Sms sendPhoneVerificationSms(String phoneNumber, PhoneVerification verification) {
        String smsBody = getAppProperty("sms.verificationPhrase") + " " + verification.getVerificationCode();
        Sms sms = new Sms();
        sms.setRecipient(phoneNumber);
        sms.setBody(smsBody);
        sendSms(sms);
        return sms;
    }

    @Override
    public List<Sms> findFromDate(Date date) {
        return smsResourceRepo.findByDateCreatedBetween(date, new Date());
    }

    @Override
    public SmsDeliveryReport handleDeliveryReport(Map<String, Object> deliveryReport) {
        SmsDeliveryReport report = createSmsDeliveryReport(deliveryReport);
        Optional<Sms> optionalSms = findByMessageId(report.getMessageId());
        if (optionalSms.isPresent()) {
            Sms sms = optionalSms.get();
            if (DELIVERED_TO_GATEWAY.equals(report.getStatus())) {
                sms.setSent(true);
            } else if (RECEIVED_BY_RECIPIENT.equals(report.getStatus())) {
                sms.setDelivered(true);
            }
            update(sms);
            return saveSmsDeliveryReport(report);
        } else {
            throw new ApplicationException("Sms with message id: " + report.getMessageId() + " not found");
        }
    }

    @Override
    public JpaRepository<Sms, Long> getRepo() {
        return smsResourceRepo;
    }

    @Override
    public int countByRecipient(String recipient) {
        return smsResourceRepo.countByRecipient(recipient);
    }

    @Override
    public int countByRecipientImei(String recipientImei) {
        return smsResourceRepo.countByRecipientImei(recipientImei);
    }

    @Override
    public List<Sms> findByRecipientExcludingIds(String recipient, List<Long> ids) {
        return smsResourceRepo.findByRecipient(recipient).stream().filter(call -> !ids.contains(call.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse sendSms(Sms sms) {
        return sendViaClickatel(sms);
    }

    @Override
    public MessageResponse forwardSms(Sms sms, String to) {
        Sms forwarded = new Sms();
        forwarded.setBody(sms.getFormattedString());
        forwarded.setRecipient(to);
        return sendViaClickatel(forwarded);
    }

    private String sendViaNexmo(Sms sms, String to) {
        String apiSecret = getAppProperty(SMS_API_SECRET);
        String apiKey = getAppProperty(SMS_API_KEY);
        NexmoClient client = new NexmoClient.Builder().apiKey(apiKey).apiSecret(apiSecret).build();
        String messageText = sms.getBody();
        if (to == null) {
            to = getAppProperty(SMS_DEFAULT_RECIPIENT);
        }
        String from = sms.getSender() != null ? sms.getSender() : "Vonage APIs";
        TextMessage message = new TextMessage(from, to, messageText);
        String result = "";
        try {
            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
            for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
                result += responseMessage + "\n";
            }
            return result;
        } catch (IOException | NexmoClientException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
    }

    private MessageResponse sendViaClickatel(Sms sms) {
        RequestPackage requestPackage = getRequestPackage(sms);
        String messageResponseData = HttpManager.getData(requestPackage);
        MessageResponse response = parseMessageResponse(messageResponseData);
        if (sms.getSender() == null) {
            sms.setSender(API_PHONE_NUMBER);
        }
        sms.setMessageId(response.getMessages().get(0).getApiMessageId());
        sms.setDeliveryReportRequested(true);
        saveMessageResponse(response);
        save(sms);
        return response;
    }

    @Override
    public MessageResponse saveMessageResponse(MessageResponse response) {
        Date date = new Date();
        setDateBeforeSave(response.getMessages(), date);
        setDateBeforeSave(response, date);
        saveMessages(response.getMessages());
        return messageResponseRepo.save(response);
    }

    private List<Message> saveMessages(List<Message> messages) {
        return messageRepo.saveAll(messages);
    }

    private MessageResponse parseMessageResponse(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray array = jsonObject.getJSONArray("messages");
        MessageResponse response = new MessageResponse();
        response.setMessages(new ArrayList<>());
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            response.getMessages().add(parseMessage(o));
        }
        return response;
    }

    private Message parseMessage(JSONObject o) {
        Message message = new Message();
        message.setApiMessageId(o.optString("apiMessageId", null));
        message.setTo(o.optString("to", null));
        message.setAccepted(o.getBoolean("accepted"));
        message.setError(o.optString("error", null));
        BigInteger errorCode = o.optBigInteger("errorCode", null);
        if (errorCode != null) {
            message.setErrorCode(errorCode.intValue());
        }
        message.setErrorDescription(o.optString("errorDescription", null));
        return message;
    }

    private RequestPackage getRequestPackage(Sms sms, String to) {
        return getSmsRequestPackageViaClickatel(to, sms.getFormattedString());
    }

    private RequestPackage getRequestPackage(Sms sms) {
        return getSmsRequestPackageViaClickatel(sms.getRecipient(), sms.getBody());
    }

    private RequestPackage getSmsRequestPackageViaClickatel(String to, String content) {
        String clickatelUrl = getAppProperty(CLICKATEL_URL_KEY);
        String apiKey = getAppProperty(CLICKATEL_SMS_API_KEY);
        RequestPackage requestPackage = new RequestPackage("GET", clickatelUrl);
        requestPackage.setParam("apiKey", apiKey);
        requestPackage.setParam("to", to);
        requestPackage.setParam("content", content);
        return requestPackage;
    }

    @Override
    public Object deleteByRecipientIn(List<String> phones) {
        return smsResourceRepo.deleteByRecipientIn(phones);
    }

    @Override
    public Map<String, Long> verificationForMailedSmsesByRecipient(String recipient) {
        return new HashMap<>();
    }

    @Override
    public List<SmsDeliveryReport> findAllSmsDeliveryReports() {
        return smsDeliveryReportRepo.findAll();
    }

    @Override
    public SmsDeliveryReport saveSmsDeliveryReport(SmsDeliveryReport report) {
        if (report.getId() == null) {
            report.setDateCreated(new Date());
        }
        return smsDeliveryReportRepo.save(report);
    }

    @Override
    public List<SmsDeliveryReport> findSmsDeliveryReportsByMessageId(String messageId) {
        return smsDeliveryReportRepo.findByMessageId(messageId);
    }

    @Override
    public List<SmsDeliveryReport> findSmsDeliveryReportsByRecipient(String recipient) {
        return smsDeliveryReportRepo.findByRecipient(recipient);
    }

    @Override
    public SmsDeliveryReport createSmsDeliveryReport(Map<String, Object> deliveryReport) {
        SmsDeliveryReport report = new SmsDeliveryReport(
                getString(deliveryReport, "integrationName"),
                getString(deliveryReport, "messageId"),
                getString(deliveryReport, "requestId"),
                getString(deliveryReport, "clientMessageId"),
                getString(deliveryReport, "to"),
                getString(deliveryReport, "from"),
                getString(deliveryReport, "timestamp"),
                getString(deliveryReport, "status"),
                getString(deliveryReport, "statusDescription"),
                getInteger(deliveryReport, "statusCode")
        );
        return report;
    }

    @Override
    public Optional<Sms> findByMessageId(String messageId) {
        return smsResourceRepo.findByMessageId(messageId);
    }

    private String getString(Map<String, Object> deliveryReport, String key) {
        Object obj = deliveryReport.get(key);
        if (obj != null) {
            return String.valueOf(obj);
        }
        return null;
    }

    private Integer getInteger(Map<String, Object> deliveryReport, String key) {
        Object obj = deliveryReport.get(key);
        if (obj != null) {
            return Integer.parseInt(String.valueOf(obj));
        }
        return null;
    }

}
