package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.DevContactServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DevContactServiceProxy extends BaseServiceImpl<DevContact> implements DevContactService {

    private final DevContactService service;

    public DevContactServiceProxy(Context context) {
        service = new DevContactServiceImpl(context);
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
    public void saveOnDevice(DevContact contact) {
        service.saveOnDevice(contact);
    }

    @Override
    public List<DevContact> getActualDevContacts() {
        return service.getActualDevContacts();
    }

    @Override
    public void sortByNameAsc(ArrayList<DevContact> devContacts) {
        service.sortByNameAsc(devContacts);
    }

    @Override
    public void save(Long devId, List<DevContact> contacts) {
        boolean connection = service.openConnection();
        service.save(devId, contacts);
        service.closeConnectionIf(connection);
    }

    @Override
    public List<DevContact> findByDeviceId(Long deviceId) {
        boolean openConnection = service.openConnection();
        List<DevContact> contacts = service.findByDeviceId(deviceId);
        service.closeConnectionIf(openConnection);
        return contacts;
    }

    @Override
    public List<DevContact> toDevContacts(List<SimContact> simContacts) {
        return service.toDevContacts(simContacts);
    }

    @Override
    public void integrateContactsExternally(Device device) {
        boolean connection = service.openConnection();
        boolean transaction = service.beginTransaction();
        boolean remoteConnection = service.openRemoteConnection();
        boolean remoteTransaction = service.beginRemoteTransaction();
        service.integrateContactsExternally(device);
        service.endRemoteTransactionIf(remoteTransaction);
        service.closeRemoteConnectionIf(remoteConnection);
        service.endTransactionIf(transaction);
        service.closeConnectionIf(connection);
    }

    @Override
    public List<DevContact> findRemoteContactsMissingInternally(String imei) {
        boolean openConnection = service.openConnection();
        boolean openRemoteConnection = service.openRemoteConnection();
        List<DevContact> devContacts = service.findRemoteContactsMissingInternally(imei);
        service.closeRemoteConnectionIf(openRemoteConnection);
        service.closeConnectionIf(openConnection);
        return devContacts;
    }

    @Override
    public void verify(Device device, List<DevContact> contacts) {
        boolean openConnection = service.openConnection();
        boolean transaction = service.beginTransaction();
        boolean openRemoteConnection = service.openRemoteConnection();
        boolean remoteTransaction = service.beginRemoteTransaction();
        service.verify(device, contacts);
        service.endRemoteTransactionIf(remoteTransaction);
        service.closeRemoteConnectionIf(openRemoteConnection);
        service.endTransactionIf(transaction);
        service.closeConnectionIf(openConnection);
    }

    @Override
    public List<List<DevContact>> findContactsWithDuplicateValuesByImei(String imei) {
        boolean openConnection = service.openConnection();
        List<List<DevContact>> contacts = service.findContactsWithDuplicateValuesByImei(imei);
        service.closeConnectionIf(openConnection);
        return contacts;
    }

    @Override
    public Map<DevContactValue, List<DevContact>> mapContactsByValues(List<DevContact> contacts) {
        return service.mapContactsByValues(contacts);
    }

    @Override
    public List<List<DevContact>> extractDuplicates(List<DevContact> contacts) {
        return service.extractDuplicates(contacts);
    }

    @Override
    public List<DevContactValue> extractContactValues(List<DevContact> contacts) {
        return service.extractContactValues(contacts);
    }

    @Override
    public void integrateContactsInternally(Device device) {
        boolean openConnection = service.openConnection();
        service.integrateContactsInternally(device);
        service.closeConnectionIf(openConnection);
    }

    @Override
    public List<DevContact> findByImei(String imei) {
        boolean openConnection = service.openConnection();
        List<DevContact> contacts = service.findByImei(imei);
        service.closeConnectionIf(openConnection);
        return contacts;
    }

    @Override
    public List<DevContact> findUnverifiedContactsByImei(String imei) {
        boolean openConnection = service.openConnection();
        List<DevContact> contacts = service.findUnverifiedContactsByImei(imei);
        service.closeConnectionIf(openConnection);
        return contacts;
    }

    @Override
    public List<Long> retrieveVerificationIdsByImei(String imei) {
        boolean openConnection = service.openConnection();
        List<Long> verificationIds = service.retrieveVerificationIdsByImei(imei);
        service.closeConnectionIf(openConnection);
        return verificationIds;
    }
}
