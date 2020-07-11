package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.SimContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DevContactService extends BaseService<DevContact> {

    void saveOnDevice(DevContact contact);

    List<DevContact> getActualDevContacts();

    void sortByNameAsc(ArrayList<DevContact> devContacts);

    void save(Long devId, List<DevContact> contacts);

    List<DevContact> findByDeviceId(Long deviceId);

    List<DevContact> toDevContacts(List<SimContact> simContacts);

    void integrateContactsExternally(Device device);

    List<DevContact> findRemoteContactsMissingInternally(String imei);

    void verify(Device device, List<DevContact> contacts);

    List<List<DevContact>> findContactsWithDuplicateValuesByImei(String imei);

    Map<DevContactValue, List<DevContact>> mapContactsByValues(List<DevContact> contacts);

    List<List<DevContact>> extractDuplicates(List<DevContact> contacts);

    List<DevContactValue> extractContactValues(List<DevContact> unverified);

    void integrateContactsInternally(Device device);

    List<DevContact> findByImei(String imei);

    List<DevContact> findUnverifiedContactsByImei(String imei);

    List<Long> retrieveVerificationIdsByImei(String imei);
}
