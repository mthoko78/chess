package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.SimContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DevContactService extends BaseService<DevContact> {

    void sortByNameAsc(ArrayList<DevContact> devContacts);

    List<DevContact> toDevContacts(List<SimContact> simContacts);

    Map<DevContactValue, List<DevContact>> mapContactsByValues(List<DevContact> contacts);

    List<List<DevContact>> extractDuplicates(List<DevContact> contacts);

    List<DevContactValue> extractContactValues(List<DevContact> unverified);

}
