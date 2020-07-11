package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.util.ConnectionWrapper;

import java.util.List;
import java.util.Map;

public class SimContactResourceRemote extends BaseResourceRemote<SimContact> {

	public SimContactResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
		super(SimContact.class, context, connectionWrapper);
	}

	public int countSimContactsBySimNo(String simNo) {
		Class<SimCard> parentEntity = SimCard.class;
		String whereClause = String.format("%s.simNo = '%s'", parentEntity.getSimpleName(), simNo);
		return countWhereJoining(parentEntity, getEntityClass(), whereClause);
	}

	public List<SimContact> findBySimCardId(Long simCardId) {
		String whereClause = String.format("%s.simCardId = %s", getEntityName(), simCardId);
		return findWhere(whereClause);
	}

	public List<SimContact> findBySimNo(String simNo) {
		Class<? extends UniqueEntity> parentEntityClass = SimCard.class;
		String whereClause = String.format("%s.simNo = '%s'", parentEntityClass.getSimpleName(), simNo);
		return findWhereJoining(parentEntityClass, getEntityClass(), whereClause);
	}

	public List<SimContact> findBySimNoWithIdsNotIn(List<Long> ids, String simNo) {
		if (ids.isEmpty()) {
			return findBySimNo(simNo);
		}
		Class<SimCard> parentEntityClass = SimCard.class;
		String idsValues = ids.toString().replaceAll("[\\[\\]]", "");
		String whereClause = String.format("%s.simNo = '%s' AND %s.id NOT IN (%s)", parentEntityClass.getSimpleName(),
				simNo, getEntityName(), idsValues);
		List<SimContact> contacts = findWhereJoining(parentEntityClass, getEntityClass(), whereClause);
		return contacts;
	}

	public Map<String, Long> retrieveVerificationBySimNo(String simNo) {
		return extractVerification(findBySimNo(simNo));
	}

}
