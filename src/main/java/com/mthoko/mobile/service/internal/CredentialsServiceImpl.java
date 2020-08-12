package com.mthoko.mobile.service.internal;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.resource.CredentialsResourceRemote;
import com.mthoko.mobile.service.CredentialsService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class CredentialsServiceImpl extends BaseServiceImpl implements CredentialsService {

	private final CredentialsResourceRemote credentialsResourceRemote;

	public CredentialsServiceImpl() {
		credentialsResourceRemote = new CredentialsResourceRemote(new ConnectionWrapper(null));
	}

	@Override
	public BaseResourceRemote<Credentials> getResource() {
		return credentialsResourceRemote;
	}
}
