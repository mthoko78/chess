package com.mthoko.learners.domain.devcontact;

import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.simcontact.SimContact;

import java.util.List;
import java.util.Map;

public interface DevContactService extends BaseService<DevContact> {

	List<DevContact> sortByNameAsc(List<DevContact> devContacts);

	List<DevContact> toDevContacts(List<SimContact> simContacts);

	Map<DevContactValue, List<DevContact>> mapContactsByValues(List<DevContact> contacts);

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

	List<DevContactValue> extractDuplicateValues(List<DevContact> contacts);

	void deleteValues(List<DevContactValue> values);

	List<DevContact> extractContactsWithEmptyValues(List<DevContact> contacts);

}
