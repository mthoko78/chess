package com.mthoko.learners.domain.devcontact;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.domain.device.Device;
import com.mthoko.learners.domain.device.DeviceRepo;
import com.mthoko.learners.domain.simcontact.SimContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
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

    private final DevContactRepo devContactRepo;

    private final DevContactValueRepo devContactValueRepo;

    private final DeviceRepo deviceRepo;

    @Autowired
    public DevContactServiceImpl(DevContactRepo resource, DevContactValueRepo valueResource, DeviceRepo deviceRepo) {
        super();
        this.devContactRepo = resource;
        this.devContactValueRepo = valueResource;
        this.deviceRepo = deviceRepo;
    }

    @Override
    public List<DevContact> sortByNameAsc(List<DevContact> devContacts) {
        devContacts.sort((devContact, devContact2) -> {
            int result = devContact.getName().compareTo(devContact2.getName());
            return result < 0 ? -1 : result > 0 ? 1 : 0;
        });
        return devContacts;
    }

    @Override
    public List<DevContact> toDevContacts(List<SimContact> simContacts) {
        List<DevContact> devContacts = new ArrayList<>();
        for (SimContact simContact : simContacts) {
            DevContact devContact = new DevContact();
            devContact.setName(simContact.getName());
            devContact.getValues().add(new DevContactValue(TYPE_MOBILE, simContact.getPhone()));
            devContacts.add(devContact);
        }
        return devContacts;
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
    public List<DevContactValue> extractContactValues(List<DevContact> contacts) {
        List<DevContactValue> values = new ArrayList<>();
        for (DevContact contact : contacts) {
            values.addAll(contact.getValues());
        }
        return values;
    }

    @Override
    public List<DevContact> findByImei(String imei) {
        Optional<Device> optionalDevice = deviceRepo.findByImei(imei);
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            return devContactRepo.findByDevId(device.getId());
        }
        return new ArrayList<>();
    }

    @Override
    public List<DevContact> findByImeiExcludingIds(String imei, List<Long> ids) {
        Optional<Device> optionalDevice = deviceRepo.findByImei(imei);
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            if (ids.isEmpty()) {
                return devContactRepo.findByDevId(device.getId());
            }
            return devContactRepo.findByDevIdAndIdNotIn(device.getId(), ids);
        }
        return new ArrayList<>();
    }

    @Override
    public int countByImei(String imei) {
        return devContactRepo.countByImei(imei);
    }

    @Override
    public List<DevContact> findByImeiWithEmptyValues(String imei) {
        return findByImei(imei).stream().filter(contact -> contact.getValues().isEmpty()).collect(Collectors.toList());
    }

    @Override
    public List<DevContact> deleteWithEmptyValues(String imei) {
        List<DevContact> deletedContacts = findByImeiWithEmptyValues(imei);
        deleteAll(deletedContacts);
        return deletedContacts;
    }

    @Override
    public List<DevContact> findRedundantByImei(String imei) {
        List<DevContact> contacts = findByImei(imei);
        return extractRedundantContacts(contacts);
    }

    @Override
    public List<DevContact> optimizeByImei(String imei, boolean delete) {
        List<DevContact> contacts = findByImei(imei);
        List<DevContact> optimized = optimize(contacts);
        if (delete) {
            removeAll(contacts, optimized);
            devContactRepo.deleteAll(contacts);
            devContactValueRepo.deleteAll(extractRedundantValues(contacts));
        }
        return optimized;
    }

    public List<DevContact> optimize(List<DevContact> contacts) {
        List<DevContactValue> redundantValues = extractRedundantValues(contacts);
        for (DevContact contact : contacts) {
            removeAll(contact.getValues(), redundantValues);
        }
        removeAll(contacts, extractContactsWithEmptyValues(contacts));
        return contacts;
    }

    @Override
    public List<DevContactValue> findRedundantValuesByImei(String imei) {
        return extractRedundantValues(findByImei(imei));
    }

    private List<DevContactValue> extractRedundantValues(List<DevContact> contacts) {
        List<String> uniqueIds = new ArrayList<>();
        List<DevContactValue> redundantValues = new ArrayList<>();
        for (DevContactValue devContactValue : extractValuesOrderedByIdDesc(contacts)) {
            String uniqueIdentifier = devContactValue.getUniqueIdentifier().replaceAll("\\s+", "");
            if (!uniqueIds.contains(uniqueIdentifier)) {
                uniqueIds.add(uniqueIdentifier);
            } else {
                redundantValues.add(devContactValue);
            }
        }
        return redundantValues;
    }

    private List<DevContactValue> extractValuesOrderedByIdDesc(List<DevContact> contacts) {
        List<DevContactValue> duplicateContactValues = new ArrayList<>();
        for (DevContact devContact : contacts) {
            for (DevContactValue devContactValue : devContact.getValues()) {
                duplicateContactValues.add(devContactValue);
            }
        }
        duplicateContactValues.sort(new ContactComparator());
        return duplicateContactValues;
    }

    private List<DevContact> extractRedundantContacts(List<DevContact> contacts) {
        List<String> uniqueIds = new ArrayList<>();
        List<DevContact> duplicateContacts = new ArrayList<>();
        for (DevContact devContact : contacts) {
            if (!uniqueIds.contains(devContact.getUniqueIdentifier())) {
                uniqueIds.add(devContact.getUniqueIdentifier());
            } else {
                duplicateContacts.add(devContact);
            }
        }
        return duplicateContacts;
    }

    @Override
    public List<DevContactValue> deleteRedundantValuesByImei(String imei) {
        List<DevContactValue> redundant = findRedundantValuesByImei(imei);
        devContactValueRepo.deleteAll(redundant);
        return redundant;
    }

    @Override
    public List<DevContact> extractContactsWithEmptyValues(List<DevContact> contacts) {
        return contacts.stream().filter((contact) -> contact.getValues() == null || contact.getValues().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<DevContact> saveAll(List<DevContact> entities) {
        setDateBeforeSave(entities, new Date());
        devContactValueRepo.saveAll(extractContactValues(entities));
        return super.saveAll(entities);
    }

    @Override
    public DevContact save(DevContact entity) {
        setDateBeforeSave(entity, new Date());
        devContactValueRepo.saveAll(entity.getValues());
        return super.save(entity);
    }

    @Override
    public <V extends UniqueEntity> List<V> setDateBeforeSave(List<V> entities, Date date) {
        List<DevContactValue> values = extractContactValues((List<DevContact>) entities);
        setDateBeforeSave(values, date);
        super.setDateBeforeSave(entities, date);
        return entities;
    }

    @Override
    public <V extends UniqueEntity> V setDateBeforeSave(V entity, Date date) {
        super.setDateBeforeSave(((DevContact) entity).getValues(), date);
        super.setDateBeforeSave(entity, date);
        return entity;
    }

    @Override
    public JpaRepository<DevContact, Long> getRepo() {
        return devContactRepo;
    }

    @Override
    public void deleteByDevIdIn(List<Long> deviceIds) {
        devContactRepo.deleteByDevIdIn(deviceIds);
    }

    @Override
    public List<DevContactValue> extractDuplicateValues(List<DevContact> contacts) {
        return extractDuplicates(extractContactValues(contacts));
    }

    @Override
    public void deleteValues(List<DevContactValue> values) {
        devContactValueRepo.deleteAll(values);
    }

}