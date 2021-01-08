package com.mthoko.mobile.service;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.entity.SimContact;

public interface DevContactService extends BaseService<DevContact> {

	List<DevContact> sortByNameAsc(List<DevContact> devContacts);

	List<DevContact> toDevContacts(List<SimContact> simContacts);

	Map<DevContactValue, List<DevContact>> mapContactsByValues(List<DevContact> contacts);

	List<List<DevContact>> extractDuplicates(List<DevContact> contacts);

	List<DevContactValue> extractContactValues(List<DevContact> unverified);

	List<DevContact> findByImei(String imei);

	List<DevContact> findByImeiExcludingIds(String imei, List<Long> ids);

	int countByImei(String imei);

	List<DevContact> findByImeiWithEmptyValues(String imei);

	List<DevContact> deleteWithEmptyValues(String imei);

	List<DevContact> findRedundantByImei(String imei);

	List<DevContactValue> findRedundantValuesByImei(String imei);

	List<DevContactValue> deleteRedundantValuesByImei(String imei);

	List<DevContact> optimizeByImei(String imei, boolean delete);

	List<DevContact> optimize(List<DevContact> contacts);

	void deleteByDevIdIn(List<Long> deviceIds);

}
