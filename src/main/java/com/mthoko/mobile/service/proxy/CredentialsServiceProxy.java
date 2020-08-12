package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.service.CredentialsService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.CredentialsServiceImpl;

public class CredentialsServiceProxy extends BaseServiceImpl implements CredentialsService {

	private final CredentialsServiceImpl service;

	public CredentialsServiceProxy() {
		service = new CredentialsServiceImpl();
	}

	@Override
	public BaseResourceRemote<Credentials> getResource() {
		return service.getResource();
	}

}
