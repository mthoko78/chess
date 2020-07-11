package com.mthoko.mobile.service.internal;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.MemberResourceRemote;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class MemberServiceImpl extends BaseServiceImpl<Member> implements MemberService {

    private final MemberResourceRemote memberResourceRemote;

    public MemberServiceImpl() {
        memberResourceRemote = new MemberResourceRemote(new ConnectionWrapper(null));
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return memberResourceRemote;
    }

}
