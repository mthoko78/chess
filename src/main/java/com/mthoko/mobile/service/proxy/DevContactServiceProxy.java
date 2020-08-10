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

public class DevContactServiceProxy extends BaseServiceImpl implements DevContactService {

	private final DevContactServiceImpl service;

	public DevContactServiceProxy() {
		service = new DevContactServiceImpl();
	}

	@Override
	public BaseResourceRemote<DevContact> getResource() {
		return service.getResource();
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

	@Override
	public List<DevContact> findByImei(String imei) {
		boolean openConnection = service.openConnection();
		List<DevContact> deviceContacts = service.findByImei(imei);
		service.closeConnectionIf(openConnection);
		return deviceContacts;
	}

	@Override
	public List<DevContact> findByImeiWithIdsNotIn(List<Long> remoteIds, String imei) {
		boolean openConnection = service.openConnection();
		List<DevContact> deviceContacts = service.findByImeiWithIdsNotIn(remoteIds, imei);
		service.closeConnectionIf(openConnection);
		return deviceContacts;
	}

	@Override
	public int countByImei(String imei) {
		boolean openConnection = service.openConnection();
		int count = service.countByImei(imei);
		service.closeConnectionIf(openConnection);
		return count;
	}

}
