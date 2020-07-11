package com.mthoko.mobile.resource.remote;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.util.ConnectionWrapper;

public class DevContactResourceRemote extends BaseResourceRemote<DevContact> {

	public DevContactResourceRemote(ConnectionWrapper connectionWrapper) {
		super(DevContact.class, connectionWrapper);
	}

	public List<DevContact> findByImei(String imei) {
		Class<Device> parentEntityClass = Device.class;
		String whereClause = String.format("%s.imei = '%s'", parentEntityClass.getSimpleName(), imei);
		return findWhereJoining(parentEntityClass, getEntityClass(), whereClause);
	}

	public List<DevContact> findByImeiWithIdsNotIn(List<Long> ids, String imei) {
		if (ids.isEmpty()) {
			return findByImei(imei);
		}
		String idsValues = ids.toString().replaceAll("[\\[\\]]", "");
		String parentEntity = Device.class.getSimpleName();
		String whereClause = String.format("%s.imei = '%s' AND %s.id NOT IN (%s)", parentEntity, imei,
				getEntityName(), idsValues);
		Class<? extends UniqueEntity> parentEntityClass = Device.class;
		return findWhereJoining(parentEntityClass, getEntityClass(), whereClause);
	}

	public int countByImei(String imei) {
		Class<Device> parentEntity = Device.class;
		return countWhereJoining(parentEntity, getEntityClass(),
				String.format("%s.imei = '%s'", parentEntity.getSimpleName(), imei));
	}

	public Map<String, Long> retrieveVerificationByImei(String imei) {
		return extractVerification(findByImei(imei));
	}

}