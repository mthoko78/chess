package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.CredentialsService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.CredentialsServiceImpl;

public class CredentialsServiceProxy extends BaseServiceImpl<Credentials> implements CredentialsService {

    private final CredentialsServiceImpl service;

    public CredentialsServiceProxy(Context context) {
        service = new CredentialsServiceImpl(context);
    }

    @Override
    public BaseResource getResource() {
        return service.getResource();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

    @Override
    public void setContext(Context context) {
        service.setContext(context);
    }
}
