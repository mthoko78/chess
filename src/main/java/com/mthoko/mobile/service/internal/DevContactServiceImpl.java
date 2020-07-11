package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.DevContactResource;
import com.mthoko.mobile.resource.internal.DevContactValueResource;
import com.mthoko.mobile.resource.internal.DeviceResource;
import com.mthoko.mobile.resource.internal.SimContactResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.DevContactResourceRemote;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevContactServiceImpl extends BaseServiceImpl<DevContact> implements DevContactService {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_HOME = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_WORK = 3;
    public static final int TYPE_FAX_WORK = 4;
    public static final int TYPE_FAX_HOME = 5;
    public static final int TYPE_PAGER = 6;
    public static final int TYPE_OTHER = 7;
    public static final int TYPE_CALLBACK = 8;
    public static final int TYPE_CAR = 9;
    public static final int TYPE_COMPANY_MAIN = 10;
    public static final int TYPE_ISDN = 11;
    public static final int TYPE_MAIN = 12;
    public static final int TYPE_OTHER_FAX = 13;
    public static final int TYPE_RADIO = 14;
    public static final int TYPE_TELEX = 15;
    public static final int TYPE_TTY_TDD = 16;
    public static final int TYPE_WORK_MOBILE = 17;
    public static final int TYPE_WORK_PAGER = 18;
    public static final int TYPE_ASSISTANT = 19;
    public static final int TYPE_MMS = 20;

    public static final String[] STRING_TYPE = new String[21];

    static {
        STRING_TYPE[TYPE_DEFAULT] = "DEFAULT";
        STRING_TYPE[TYPE_HOME] = "HOME";
        STRING_TYPE[TYPE_MOBILE] = "MOBILE";
        STRING_TYPE[TYPE_WORK] = "WORK";
        STRING_TYPE[TYPE_FAX_WORK] = "FAX_WORK";
        STRING_TYPE[TYPE_FAX_HOME] = "FAX_HOME";
        STRING_TYPE[TYPE_PAGER] = "PAGER";
        STRING_TYPE[TYPE_OTHER] = "OTHER";
        STRING_TYPE[TYPE_CALLBACK] = "CALLBACK";
        STRING_TYPE[TYPE_CAR] = "CAR";
        STRING_TYPE[TYPE_COMPANY_MAIN] = "COMPANY_MAIN";
        STRING_TYPE[TYPE_ISDN] = "ISDN";
        STRING_TYPE[TYPE_MAIN] = "MAIN";
        STRING_TYPE[TYPE_OTHER_FAX] = "OTHER_FAX";
        STRING_TYPE[TYPE_RADIO] = "RADIO";
        STRING_TYPE[TYPE_TELEX] = "TELEX";
        STRING_TYPE[TYPE_TTY_TDD] = "TTY_TDD";
        STRING_TYPE[TYPE_WORK_MOBILE] = "WORK_MOBILE";
        STRING_TYPE[TYPE_WORK_PAGER] = "WORK_PAGER";
        STRING_TYPE[TYPE_ASSISTANT] = "ASSISTANT";
        STRING_TYPE[TYPE_MMS] = "MMS";
    }

    private final DevContactResource devContactResource;
    private final DevContactResourceRemote devContactResourceRemote;
    private final DeviceResource deviceResource;
    private final SimContactResource simContactResource;
    private final DevContactValueResource devContactValueResource;

    public DevContactServiceImpl(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper();
        devContactResourceRemote = new DevContactResourceRemote(context, new ConnectionWrapper(null));
        devContactResource = new DevContactResource(context, databaseWrapper);
        deviceResource = new DeviceResource(context, databaseWrapper);
        simContactResource = new SimContactResource(context, databaseWrapper);
        devContactValueResource = new DevContactValueResource(context, databaseWrapper);
    }

    @Override
    public void saveOnDevice(DevContact contact) {
        devContactResource.saveOnDevice(contact);
    }

    @Override
    public List<DevContact> getActualDevContacts() {
        return devContactResource.getActualDevContacts();
    }

    @Override
    public void sortByNameAsc(ArrayList<DevContact> devContacts) {
        for (int i = 0; i < devContacts.size(); i++) {
            int indexOfMin = i;
            for (int j = i + 1; j < devContacts.size(); j++)
                if (devContacts.get(j).getName().compareTo(devContacts.get(indexOfMin).getName()) < 0)
                    indexOfMin = j;
            if (indexOfMin != i) {
                // need to swap max with i
                DevContact temp = devContacts.set(i, devContacts.get(indexOfMin));
                devContacts.set(indexOfMin, temp);
            }
        }
    }

    @Override
    public void save(Long devId, List<DevContact> contacts) {
        for (DevContact contact : contacts) {
            contact.setDevId(devId);
        }
        saveAll(contacts);
    }

    @Override
    public void setContext(Context context) {
        devContactResource.setContext(context);
        deviceResource.setContext(context);
        simContactResource.setContext(context);
        devContactValueResource.setContext(context);
    }

    @Override
    public List<DevContact> findByDeviceId(Long deviceId) {
        return devContactResource.findByDeviceId(deviceId);
    }

    @Override
    public List<DevContact> toDevContacts(List<SimContact> simContacts) {
        List<DevContact> devContacts = new ArrayList<>();
        for (SimContact simContact : simContacts) {
            DevContact devContact = new DevContact();
            devContact.setName(simContact.getName());
            devContact.getValues().add(new DevContactValue(null, TYPE_MOBILE, simContact.getPhone()));
            devContacts.add(devContact);
        }
        return devContacts;
    }

    @Override
    public BaseResource getResource() {
        return devContactResource;
    }

    @Override
    public Context getContext() {
        return devContactResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return devContactResourceRemote;
    }

    @Override
    public void integrateContactsExternally(Device device) {
        if (device == null || device.getImei() == null) {
            return;
        }
        String imei = device.getImei();
        List<DevContact> unverified = devContactResource.findUnverifiedByImei(imei);
        if (!unverified.isEmpty()) {
            verify(device, unverified);
        }
        List<DevContact> remoteContacts = findRemoteContactsMissingInternally(imei);
        if (!remoteContacts.isEmpty()) {
            devContactResource.saveAll(remoteContacts);
            if (getImei().equals(device.getImei())) {
                // TODO: check if current user owns device before the operation below
                devContactResource.saveAllOnDevice(remoteContacts);
            }
        }
    }

    @Override
    public List<DevContact> findRemoteContactsMissingInternally(String imei) {
        List<Long> remoteIds = devContactResource.retrieveVerificationIdsByImei(imei);
        List<DevContact> contacts = devContactResourceRemote.findByImeiWithIdsNotIn(remoteIds, imei);
        return contacts;
    }

    @Override
    public void verify(Device device, List<DevContact> contacts) {
        List<DevContact> unverified = new ArrayList<>(contacts);
        removeVerified(unverified);
        if (unverified.isEmpty()) {
            return;
        }
        String imei = device.getImei();
        int count = devContactResourceRemote.countByImei(imei);
        if (count > 0) {
            Map<String, Long> verification = devContactResourceRemote.retrieveVerificationByImei(imei);
            verifyAll(unverified, verification);
            removeVerified(unverified);
        }
        if (!unverified.isEmpty()) {
            devContactResourceRemote.saveAll(unverified);
            updateAll(unverified);
        }
    }

    @Override
    public List<List<DevContact>> findContactsWithDuplicateValuesByImei(String imei) {
        return extractDuplicates(devContactResource.findByImei(imei));
    }

    private Map<DevContactValue, List<DevContact>> filterDuplicates(List<DevContact> contacts) {
        Map<DevContactValue, List<DevContact>> contactsMappedByValues = mapContactsByValues(contacts);
        Map<DevContactValue, List<DevContact>> duplicateValues = new HashMap<>();
        for (Map.Entry<DevContactValue, List<DevContact>> entry : contactsMappedByValues.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicateValues.put(entry.getKey(), entry.getValue());
            }
        }
        return duplicateValues;
    }

    private void removeDuplicateValues(List<DevContact> contacts, boolean deletePermanently) {
        Map<DevContactValue, List<DevContact>> devContactValueListMap = filterDuplicates(contacts);
        for (Map.Entry<DevContactValue, List<DevContact>> entry : devContactValueListMap.entrySet()) {
            DevContactValue contactValue = entry.getKey();
            List<DevContact> devContacts = entry.getValue();
            for (int i = 1; i < devContacts.size(); i++) {
                devContacts.get(i).getValues().remove(contactValue);
                if (deletePermanently) {
                    devContactValueResource.delete(contactValue);
                }
            }
        }
    }

    @Override
    public Map<DevContactValue, List<DevContact>> mapContactsByValues(List<DevContact> contacts) {
        Map<DevContactValue, List<DevContact>> contactsMappedByValues = new HashMap<>();
        for (DevContact contact : contacts) {
            for (DevContactValue value : contact.getValues()) {
                if (!contactsMappedByValues.containsKey(value)) {
                    contactsMappedByValues.put(value, new ArrayList<DevContact>());
                }
                contactsMappedByValues.get(value).add(contact);
            }
        }
        return contactsMappedByValues;
    }

    @Override
    public List<List<DevContact>> extractDuplicates(List<DevContact> contacts) {
        List<List<DevContact>> duplicates = new ArrayList<>();
        for (List<DevContact> devContacts : filterDuplicates(contacts).values()) {
            duplicates.add(devContacts);
        }
        return duplicates;
    }

    @Override
    public void verifyAll(List<DevContact> devContacts, Map<String, Long> verification) {
        devContactResource.verifyAll(devContacts, verification);
        devContactValueResource.verifyAll(extractContactValues(devContacts), verification);
    }

    @Override
    public void verify(DevContact entity, Map<String, Long> verification) {
        devContactResource.verify(entity, verification);
        devContactValueResource.verifyAll(entity.getValues(), verification);
    }

    @Override
    public List<DevContactValue> extractContactValues(List<DevContact> unverified) {
        List<DevContactValue> values = new ArrayList<>();
        for (DevContact contact : unverified) {
            values.addAll(contact.getValues());
        }
        return values;
    }

    @Override
    public void integrateContactsInternally(Device device) {
        if (device == null || !getImei().equals(device.getImei())) {
            return;
        }

        List<DevContact> internalContacts = devContactResource.findByImei(device.getImei());
        List<DevContact> contactsInDevice = getActualDevContacts();
        List<DevContact> contactsNotInDevice = new ArrayList<>(internalContacts);
        contactsNotInDevice.removeAll(contactsInDevice);
        contactsInDevice.removeAll(internalContacts);
        if (!contactsInDevice.isEmpty()) {
            for (DevContact devContact : contactsInDevice) {
                devContact.setDevId(device.getId());
            }
            devContactResource.saveAll(contactsInDevice);
        }
        if (!contactsNotInDevice.isEmpty()) {
            devContactResource.saveAllOnDevice(contactsNotInDevice);
        }
    }

    @Override
    public List<DevContact> findByImei(String imei) {
        return devContactResource.findByImei(imei);
    }

    @Override
    public List<DevContact> findUnverifiedContactsByImei(String imei) {
        String whereClause = String.format("%s.imei = '%s' AND %s.verificationId IS NULL", Device.class.getSimpleName(), imei, Device.class.getSimpleName());
        return devContactResource.findWhereJoining(Device.class, DevContact.class, whereClause);
    }

    @Override
    public List<Long> retrieveVerificationIdsByImei(String imei) {
        String whereClause = String.format("%s.imei = '%s' AND %s.verificationId IS NOT NULL", Device.class.getSimpleName(), imei, Device.class.getSimpleName());
        return devContactResource.retrieveVerificationIdsJoiningWhere(Device.class, DevContact.class, whereClause);
    }
}