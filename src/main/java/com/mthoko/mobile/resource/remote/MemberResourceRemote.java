package com.mthoko.mobile.resource.remote;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.util.ConnectionWrapper;

public class MemberResourceRemote extends BaseResourceRemote<Member> {

	public MemberResourceRemote(ConnectionWrapper connectionWrapper) {
		super(Member.class, connectionWrapper);
	}

	public Member findBySimNo(String simNo) {
		Class<SimCard> parentEntityClass = SimCard.class;
		String whereClause = String.format("%s.simNo = '%s'", parentEntityClass.getSimpleName(), simNo);
		return findOneWhereJoining(getEntityClass(), parentEntityClass, whereClause);
	}

	public Member findByPhone(String phone) {
		String whereClause = String.format("%s.phone = '%s'", getEntityName(), phone);
		return findOneWhere(whereClause);
	}

	public Member findByEmail(String email) {
		String whereClause = String.format("%s.email = '%s'", getEntityName(), email);
		return findOneWhere(whereClause);
	}

}
