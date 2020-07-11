package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.MemberServiceImpl;

public class MemberServiceProxy extends BaseServiceImpl<Member> implements MemberService {

    private final MemberServiceImpl service;

    public MemberServiceProxy() {
        service = new MemberServiceImpl();
    }


    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

}
