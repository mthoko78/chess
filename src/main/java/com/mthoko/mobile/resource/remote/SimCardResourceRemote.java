package com.mthoko.mobile.resource.remote;

import java.util.List;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.util.ConnectionWrapper;

public class SimCardResourceRemote extends BaseResourceRemote<SimCard> {

	public SimCardResourceRemote(ConnectionWrapper connectionWrapper) {
		super(SimCard.class, connectionWrapper);
	}

	public SimCard findBySimNo(String simNo) {
		return findOneWhere(String.format("simNo = '%s'", simNo));
	}

	public List<SimCard> findByMemberId(Long id) {
		String whereClause = String.format("%s.memberId = " + id, getEntityName());
		return findWhere(whereClause);
	}

}