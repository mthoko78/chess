package com.mthoko.mobile.resource.remote;


import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.util.ConnectionWrapper;

public class CredentialsResourceRemote extends BaseResourceRemote<Credentials> {

	public CredentialsResourceRemote(ConnectionWrapper connectionWrapper) {
		super(Credentials.class, connectionWrapper);
	}

	public Credentials findByMemberId(Long id) {
		String whereClause = getEntityName() + ".memberId = " + id;
		return findOneWhere(whereClause);
	}
}
