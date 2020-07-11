package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.RecordedCallServiceImpl;

public class RecordedCallServiceProxy extends BaseServiceImpl<RecordedCall> implements RecordedCallService {

    private final RecordedCallServiceImpl service;

    public RecordedCallServiceProxy(Context context) {
        service = new RecordedCallServiceImpl(context);
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
    public String getDurationToString(int duration) {
        return service.getDurationToString(duration);
    }

    @Override
    public RecordedCall retrieveRecentCall() {
        boolean connection = openConnection();
        RecordedCall call = service.retrieveRecentCall();
        closeConnectionIf(connection);
        return call;
    }
}
