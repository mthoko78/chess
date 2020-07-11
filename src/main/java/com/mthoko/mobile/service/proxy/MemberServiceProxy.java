package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.MemberServiceImpl;

public class MemberServiceProxy extends BaseServiceImpl<Member> implements MemberService {

    private final MemberServiceImpl service;

    public MemberServiceProxy(Context context) {
        service = new MemberServiceImpl(context);
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
