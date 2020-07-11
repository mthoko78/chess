package com.mthoko.mobile.service.proxy;

import android.content.Context;
import android.os.Bundle;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SmsServiceImpl;

import java.util.List;

public class SmsServiceProxy extends BaseServiceImpl<Sms> implements SmsService {

    private final SmsServiceImpl service;

    public SmsServiceProxy(Context context) {
        service = new SmsServiceImpl(context);
    }

    @Override
    public BaseResource getResource() {
        return service.getResource();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

    @Override
    public void setContext(Context context) {
        service.setContext(context);
    }

    @Override
    public void integrateMessagesExternally(SimCard simCard) {
        boolean connection = service.openConnection();
        boolean transaction = service.beginTransaction();
        boolean remoteConnection = service.openRemoteConnection();
        boolean remoteTransaction = service.beginRemoteTransaction();
        service.integrateMessagesExternally(simCard);
        service.endRemoteTransactionIf(remoteTransaction);
        service.closeRemoteConnectionIf(remoteConnection);
        service.endTransactionIf(transaction);
        service.closeConnectionIf(connection);
    }

    @Override
    public List<Sms> integrateMessagesInternally(SimCard simCard) {
        boolean openConnection = service.openConnection();
        List<Sms> smsList = service.integrateMessagesInternally(simCard);
        service.closeConnectionIf(openConnection);
        return smsList;
    }

    @Override
    public List<Sms> getActualInbox() {
        boolean openConnection = service.openConnection();
        List<Sms> actualInbox = service.getActualInbox();
        service.closeConnectionIf(openConnection);
        return actualInbox;
    }

    @Override
    public List<Sms> getActualSimInbox() {
        boolean connection = openConnection();
        List<Sms> actualSimInbox = service.getActualSimInbox();
        closeConnectionIf(connection);
        return actualSimInbox;
    }

    @Override
    public List<Sms> getActualSent() {
        boolean connection = openConnection();
        List<Sms> actualSent = service.getActualSent();
        closeConnectionIf(connection);
        return actualSent;
    }

    @Override
    public List<Sms> getSmsListFromBundle(Bundle extras) {
        return service.getSmsListFromBundle(extras);
    }

    @Override
    public void saveAllToRemote(List<Sms> smses) {
        boolean remoteConnection = service.openRemoteConnection();
        boolean remoteTransaction = service.beginRemoteTransaction();
        getRemoteResource().saveAll(smses);
        service.endRemoteTransactionIf(remoteTransaction);
        service.closeRemoteConnectionIf(remoteConnection);
    }
}
