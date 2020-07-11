package com.mthoko.mobile.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.DevContactResourceRemote;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.util.ConnectionWrapper;

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

    private final DevContactResourceRemote devContactResourceRemote;

    public DevContactServiceImpl() {
        devContactResourceRemote = new DevContactResourceRemote(new ConnectionWrapper(null));
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
    public BaseResourceRemote getRemoteResource() {
        return devContactResourceRemote;
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
    public List<DevContactValue> extractContactValues(List<DevContact> unverified) {
        List<DevContactValue> values = new ArrayList<>();
        for (DevContact contact : unverified) {
            values.addAll(contact.getValues());
        }
        return values;
    }

}