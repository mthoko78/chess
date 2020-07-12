package com.mthoko.mobile.service.internal;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.SimCardResourceRemote;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class SimCardServiceImpl extends BaseServiceImpl<SimCard> implements SimCardService {

    private final SimCardResourceRemote simCardResourceRemote;

    public SimCardServiceImpl() {
        simCardResourceRemote = new SimCardResourceRemote(new ConnectionWrapper(null));
    }

    @Override
    public BaseResourceRemote getResource() {
        return simCardResourceRemote;
    }
}
