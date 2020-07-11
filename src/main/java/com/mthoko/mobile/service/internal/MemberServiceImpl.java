package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.MemberResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.MemberResourceRemote;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

public class MemberServiceImpl extends BaseServiceImpl<Member> implements MemberService {

    private final MemberResource memberResource;
    private final MemberResourceRemote memberResourceRemote;

    public MemberServiceImpl(Context context) {
        memberResource = new MemberResource(context, new DatabaseWrapper());
        memberResourceRemote = new MemberResourceRemote(context, new ConnectionWrapper(null));
    }

    @Override
    public void setContext(Context context) {
        memberResource.setContext(context);
    }

    @Override
    public BaseResource getResource() {
        return memberResource;
    }

    @Override
    public Context getContext() {
        return memberResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return memberResourceRemote;
    }

    public Member findByEmail(String email) {
        Member member = memberResource.findByEmail(email);

        return member;
    }

    public Member findByPhone(String phone) {
        Member member = memberResource.findByPhone(phone);

        return member;
    }

    public Member findDeviceOwnerByImei(String imei) {
        String whereClause = String.format("%s.imei = '%s'", Device.class.getSimpleName(), imei);
        Member member = memberResource.findOneWhereJoining(Device.class, Member.class, whereClause);

        return member;
    }

    public Member findDeviceOwnerByDeviceId(String devId) {
        String whereClause = String.format("%s.id= %s", Device.class.getSimpleName(), devId);

        Member member = memberResource.findOneWhereJoining(Device.class, Member.class, whereClause);
        return member;
    }
}
