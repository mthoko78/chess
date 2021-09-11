package com.mthoko.mobile.domain.sms;

import com.mthoko.mobile.common.controller.BaseController;
import com.mthoko.mobile.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("sms")
public class SmsController extends BaseController<Sms> {

    public static final String DELIVERED_TO_GATEWAY = "DELIVERED_TO_GATEWAY";

    public static final String RECEIVED_BY_RECIPIENT = "RECEIVED_BY_RECIPIENT";

    private final SmsService service;

    @Autowired
    public SmsController(SmsService service) {
        this.service = service;
    }

    @Override
    public BaseService<Sms> getService() {
        return service;
    }

    @RequestMapping("recipient/{recipient}")
    public List<Sms> findByRecipient(@PathVariable("recipient") String recipient) {
        return service.findByRecipient(recipient);
    }

    @RequestMapping("recipientImei/{recipientImei}")
    public List<Sms> findByRecipientImei(@PathVariable("recipientImei") String recipientImei) {
        return service.findByRecipientImei(recipientImei);
    }

    @PostMapping("excluding-ids/recipient/{recipient}")
    public List<Sms> findByRecipientExcludingIds(@PathVariable("recipient") String recipient,
                                                 @RequestBody List<Long> ids) {
        return service.findByRecipientExcludingIds(recipient, ids);
    }

    @GetMapping("count-by-recipient/{recipient}")
    public int countByRecipient(@PathVariable("recipient") String recipient) {
        return service.countByRecipient(recipient);
    }



    @GetMapping("count-by-recipientImei/{recipientImei}")
    public int countByRecipientImei(@PathVariable("recipientImei") String recipientImei) {
        return service.countByRecipientImei(recipientImei);
    }

    @GetMapping("verification-for-mailed/recipient/{recipient}")
    public Map<String, Long> verificationForMailedSmsesByRecipient(@PathVariable("recipient") String recipient) {
        return service.verificationForMailedSmsesByRecipient(recipient);
    }

    @PostMapping("send-as-mail")
    public Map<String, Long> sendAsMail(@RequestBody Sms sms) {
        return service.sendAsMail(sms);
    }

    @PostMapping("send-all-as-mail")
    public Map<String, Long> sendAllAsMail(@RequestBody List<Sms> smses) {
        return service.sendAllAsMail(smses);
    }

    @PostMapping("forward/to/{to}")
    public MessageResponse forwardSms(@RequestBody Sms sms, @PathVariable("to") String to) {
        return service.forwardSms(sms, to);
    }

    @PostMapping("send")
    public MessageResponse sendSms(@RequestBody Sms sms) {
        return service.sendSms(sms);
    }

    @PostMapping("delivery-report")
    public SmsDeliveryReport handleDeliveryReport(@RequestBody Map<String, Object> deliveryReport) {
        SmsDeliveryReport report = service.createSmsDeliveryReport(deliveryReport);
        Optional<Sms> optionalSms = findByMessageId(report.getMessageId());
        if (optionalSms.isPresent()) {
            Sms sms = optionalSms.get();
            if (DELIVERED_TO_GATEWAY.equals(report.getStatus())) {
                sms.setSent(true);
            } else if (RECEIVED_BY_RECIPIENT.equals(report.getStatus())) {
                sms.setDelivered(true);
            }
            update(sms);
        }
        return service.saveSmsDeliveryReport(report);
    }

    @GetMapping("delivery-report")
    public List<SmsDeliveryReport> findAllDeliveryReports() {
        return this.service.findAllSmsDeliveryReports();
    }

    @GetMapping("delivery-report/messageId/{messageId}")
    public List<SmsDeliveryReport> findSmsDeliveryReportsByMessageId(@PathVariable("messageId") String messageId) {
        return service.findSmsDeliveryReportsByMessageId(messageId);
    }

    @GetMapping("delivery-report/recipient/{recipient}")
    public List<SmsDeliveryReport> findSmsDeliveryReportsByRecipient(@PathVariable("recipient") String recipient) {
        return service.findSmsDeliveryReportsByRecipient(recipient);
    }

    @RequestMapping("messageId/{messageId}")
    public Optional<Sms> findByMessageId(@PathVariable("messageId") String messageId) {
        return service.findByMessageId(messageId);
    }


}
