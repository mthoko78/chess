package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SimCardServiceImpl;

public class SimCardServiceProxy extends BaseServiceImpl<SimCard> implements SimCardService {

    private final SimCardServiceImpl service;

    public SimCardServiceProxy(Context context) {
        service = new SimCardServiceImpl(context);
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

    @Override
    public SimCard findBySimNo(String simNo) {
        boolean openConnection = service.openConnection();
        SimCard simCard = service.findBySimNo(simNo);
        service.closeConnectionIf(openConnection);
        return simCard;
    }
}
