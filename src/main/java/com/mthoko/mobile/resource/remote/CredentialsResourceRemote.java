package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.util.ConnectionWrapper;

public class CredentialsResourceRemote extends BaseResourceRemote<Credentials> {

	public CredentialsResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
		super(Credentials.class, context, connectionWrapper);
	}

	public Credentials findByMemberId(Long id) {
		String whereClause = getEntityName() + ".memberId = " + id;
		return findOneWhere(whereClause);
	}
}
