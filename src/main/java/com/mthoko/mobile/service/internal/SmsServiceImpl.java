package com.mthoko.mobile.service.internal;

import java.util.ArrayList;
import java.util.List;

import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.SmsResourceRemote;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.common.MailService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class SmsServiceImpl extends BaseServiceImpl<Sms> implements SmsService {

    private final SmsResourceRemote smsResourceRemote;
    private final MailService mailService;

    public SmsServiceImpl() {
        smsResourceRemote = new SmsResourceRemote(new ConnectionWrapper(null));
        mailService = new MailService();
    }

    @Override
    public BaseResourceRemote getResource() {
        return smsResourceRemote;
    }

    public void sendAsMail(Sms sms) {
        mailService.sendEmail("New Sms", sms.getFormattedString());
    }

    public void sendAsMail(List<Sms> smsList) {
        if (smsList == null || smsList.isEmpty()) {
            return;
        }
        if (smsList.size() == 1) {
            sendAsMail(smsList.get(0));
        }
        String subject = "New Smses";
        StringBuilder body = new StringBuilder();
        for (Sms sms : smsList) {
            body.append(sms.getFormattedString());
        }
        mailService.sendEmail(subject, body.toString());
    }

    private List<Sms> findRemoteSmsesByRecipient(String recipient) {
        List<Sms> smsList = smsResourceRemote.findByRecipient(recipient);
        return smsList;
    }

    public void saveAllToRemote(List<Sms> unverified) {
        smsResourceRemote.saveAll(unverified);
    }

    private List<List<? extends UniqueEntity>> groupEntities(List<? extends UniqueEntity> entities, int groupSize) {
        List<List<? extends UniqueEntity>> result = new ArrayList<>();
        for (int i = 0; i < entities.size(); i += groupSize) {
            int lastIndex = i + groupSize;
            if (lastIndex > entities.size()) {
                lastIndex = entities.size();
            }
            result.add(entities.subList(i, lastIndex));
        }
        return result;
    }

}
