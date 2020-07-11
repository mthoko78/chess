package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.RecordedCallServiceImpl;

public class RecordedCallServiceProxy extends BaseServiceImpl<RecordedCall> implements RecordedCallService {

    private final RecordedCallServiceImpl service;

    public RecordedCallServiceProxy() {
        service = new RecordedCallServiceImpl();
    }


    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

}
