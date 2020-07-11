package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SimContactServiceImpl;

import java.util.List;

public class SimContactServiceProxy extends BaseServiceImpl<SimContact> implements SimContactService {

    private final SimContactServiceImpl service;

    public SimContactServiceProxy(Context context) {
        service = new SimContactServiceImpl(context);
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
    public void integrateContactsExternally(SimCard simCard) {
        boolean openConnection = service.openConnection();
        boolean transaction = service.beginTransaction();
        boolean remoteConnection = service.openRemoteConnection();
        boolean remoteTransaction = service.beginRemoteTransaction();
        service.integrateContactsExternally(simCard);
        service.endRemoteTransactionIf(remoteTransaction);
        service.closeRemoteConnectionIf(remoteConnection);
        service.endTransactionIf(transaction);
        service.closeConnectionIf(openConnection);
    }

    @Override
    public void integrateContactsInternally(SimCard simCard) {
        boolean openConnection = service.openConnection();
        boolean transaction = service.beginTransaction();
        service.integrateContactsInternally(simCard);
        service.endTransactionIf(transaction);
        service.closeConnectionIf(openConnection);
    }

    @Override
    public List<SimContact> retrieveUnverifiedContacts(SimCard simCard) {
        boolean remoteConnection = service.openConnection();
        List<SimContact> contacts = service.retrieveUnverifiedContacts(simCard);
        service.closeConnectionIf(remoteConnection);
        return contacts;
    }

    @Override
    public List<SimContact> findBySimCardId(Long simCardId) {
        boolean connection = service.openConnection();
        List<SimContact> contacts = service.findBySimCardId(simCardId);
        service.closeConnectionIf(connection);
        return contacts;
    }

    @Override
    public List<SimContact> getActualSimContacts() {
        return service.getActualSimContacts();
    }

    @Override
    public List<SimContact> findBySimNo(String simNo) {
        boolean connection = service.openConnection();
        List<SimContact> contacts = service.findBySimNo(simNo);
        service.closeConnectionIf(connection);
        return contacts;
    }
}
