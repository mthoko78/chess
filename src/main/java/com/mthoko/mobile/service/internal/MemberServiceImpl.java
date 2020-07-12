package com.mthoko.mobile.service.internal;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.MemberResourceRemote;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class MemberServiceImpl extends BaseServiceImpl<Member> implements MemberService {

    private final MemberResourceRemote resource;

    public MemberServiceImpl() {
        resource = new MemberResourceRemote(new ConnectionWrapper(null));
    }

    @Override
    public BaseResourceRemote<Member> getResource() {
        return resource;
    }

	@Override
	public Member findById(Long id) {
		return resource.findById(id);
	}

}
