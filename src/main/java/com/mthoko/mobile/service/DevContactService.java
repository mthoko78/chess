package com.mthoko.mobile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.SimContact;

public interface DevContactService extends BaseService {

	void sortByNameAsc(ArrayList<DevContact> devContacts);

	List<DevContact> toDevContacts(List<SimContact> simContacts);

	Map<DevContactValue, List<DevContact>> mapContactsByValues(List<DevContact> contacts);

	List<List<DevContact>> extractDuplicates(List<DevContact> contacts);

	List<DevContactValue> extractContactValues(List<DevContact> unverified);

	List<DevContact> findByImei(String imei);

	List<DevContact> findByImeiWithIdsNotIn(List<Long> remoteIds, String imei);

	int countByImei(String imei);

}
