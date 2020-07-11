package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.util.ConnectionWrapper;

import java.util.List;

public class SimCardResourceRemote extends BaseResourceRemote<SimCard> {

	public SimCardResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
		super(SimCard.class, context, connectionWrapper);
	}

	public SimCard findBySimNo(String simNo) {
		return findOneWhere(String.format("simNo = '%s'", simNo));
	}

	public List<SimCard> findByMemberId(Long id) {
		String whereClause = String.format("%s.memberId = " + id, getEntityName());
		return findWhere(whereClause);
	}

}