package com.mthoko.mobile.service.internal;

import android.content.Context;
import android.os.Bundle;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.SimCardResource;
import com.mthoko.mobile.resource.internal.SmsResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.SmsResourceRemote;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.common.MailService;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmsServiceImpl extends BaseServiceImpl<Sms> implements SmsService {

    private final SmsResource smsResource;
    private final SmsResourceRemote smsResourceRemote;
    private final MailService mailService;

    public SmsServiceImpl(Context context) {
        smsResource = new SmsResource(context, new DatabaseWrapper());
        smsResourceRemote = new SmsResourceRemote(context, new ConnectionWrapper(null));
        mailService = new MailService(context);
    }

    @Override
    public BaseResource getResource() {
        return smsResource;
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return smsResourceRemote;
    }

    @Override
    public void setContext(Context context) {
        smsResource.setContext(context);
    }

    public List<Sms> getActualInbox() {
        return smsResource.getActualInbox();
    }

    public List<Sms> getActualSimInbox() {
        return smsResource.getActualSimInbox();
    }

    public List<Sms> getActualSent() {
        return smsResource.getActualSent();
    }

    public List<Sms> getActualDrafts() {
        return smsResource.getActualDrafts();
    }

    public List<Sms> getInbox(String recipient) {
        List<Sms> inbox = smsResource.getInbox(recipient);
        return inbox;
    }

    public List<Sms> getSent(String sender) {
        List<Sms> sent = smsResource.getSent(sender);
        return sent;
    }

    public Sms getIncomingMessage(Object aObject, Bundle bundle) {
        return smsResource.getIncomingMessage(aObject, bundle);
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

    public List<Sms> integrateMessagesInternally(SimCard simCard) {
        List<Sms> internalSmses = findByPhoneOrSimNo(simCard.getPhone(), simCard.getSimNo());
        List<Sms> inbox = retrieveAllInboxMessages(simCard.getMemberId());
        inbox.removeAll(internalSmses);
        if (!inbox.isEmpty()) {
            saveAll(inbox);
            internalSmses.addAll(inbox);
        }
        return internalSmses;
    }

    public List<Sms> retrieveAllInboxMessages(Long memberId) {
        List<Sms> inbox = new ArrayList<>();
        SimCardResource simCardResource = new SimCardResource(getContext(), new DatabaseWrapper());
        simCardResource.setDatabase(getResource().getDatabase());
        Long currentSimOwner = simCardResource.findMemberIdBySimNo(getCurrentSimNo());
        Long deviceOwnerId = ServiceFactory.getDeviceService(getContext()).findMemberIdByImei(getImei());
        if (memberId == currentSimOwner) {
            inbox.addAll(this.getActualSimInbox()); // recipient is sim card phone/simcard number
        }
        if (memberId == deviceOwnerId) {
            inbox.addAll(this.getActualInbox()); // recipient is sim card phone/simcard number
        }
        return inbox;
    }

    public List<Sms> retrieveAllInboxMessages() {
        List<Sms> inbox = this.getActualSimInbox();
        inbox.addAll(this.getActualInbox());
        return inbox;
    }

    private List<Sms> findByPhoneOrSimNo(String phone, String simNo) {
        List<Sms> smsList = smsResource.findByPhoneOrSimNo(phone, simNo);
        return smsList;
    }

    private List<Sms> findByRecipient(String recipient) {
        List<Sms> smsList = smsResource.findByRecipient(recipient);
        return smsList;
    }

    private List<Sms> findRemoteSmsesByRecipient(String recipient) {
        List<Sms> smsList = smsResourceRemote.findByRecipient(recipient);
        return smsList;
    }

    public void integrateMessagesExternally(SimCard simCard) {
        if (simCard == null || !simCard.getSimNo().equals(getCurrentSimNo())) {
            return;
        }
        String recipient = simCard.getPhone();
        List<Sms> unverified = smsResource.findUnverifiedByRecipient(recipient);
        if (!unverified.isEmpty()) {
            verify(simCard, unverified);
        }
        List<Sms> remoteSmses = findRemoteSmsesMissingInternally(recipient);
        if (!remoteSmses.isEmpty()) {
            smsResource.saveAll(remoteSmses);
        }
    }

    private List<Sms> findRemoteSmsesMissingInternally(String recipient) {
        List<Long> remoteIds = smsResource.retrieveVerificationIdsByRecipient(recipient);
        List<Sms> smsList = smsResourceRemote.findByRecipientWithIdsNotIn(remoteIds, recipient);
        return smsList;
    }

    private void verify(SimCard simCard, List<Sms> smsList) {
        List<Sms> unverified = new ArrayList<>(smsList);
        removeVerified(unverified);
        if (unverified.isEmpty()) {
            return;
        }
        String recipient = simCard.getPhone();
        int count = smsResourceRemote.countSmsesByRecipient(recipient);
        if (count > 0) {
            Map<String, Long> verification = smsResourceRemote.retrieveVerificationByRecipient(recipient);
            verifyAll(unverified, verification);
            removeVerified(unverified);
        }
        if (!unverified.isEmpty()) {
            saveAllToRemote(unverified);
            updateAll(unverified);
        }
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

    public List<Sms> getSmsListFromBundle(Bundle bundle) {
        return smsResource.getSmsListFromBundle(bundle);
    }
}
