package com.mthoko.mobile.service.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.DevContactServiceImpl;

public class DevContactServiceProxy extends BaseServiceImpl<DevContact> implements DevContactService {

    private final DevContactService service;

    public DevContactServiceProxy() {
        service = new DevContactServiceImpl();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

    @Override
    public void sortByNameAsc(ArrayList<DevContact> devContacts) {
        service.sortByNameAsc(devContacts);
    }

    @Override
    public List<DevContact> toDevContacts(List<SimContact> simContacts) {
        return service.toDevContacts(simContacts);
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

}
