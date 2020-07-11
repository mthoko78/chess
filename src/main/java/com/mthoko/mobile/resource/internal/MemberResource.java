package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.List;

public class MemberResource extends BaseResource<Member> {

    public MemberResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, Member.class, databaseWrapper);
    }

    public List<Long> findMemberIdsByEmailOrPhone(String email, String phone) {
        String query = String.format(
                "SELECT id FROM %s WHERE email = '%s' OR phone = '%s'", getEntityName(),
                email, phone
        );
        List<Long> memberIds = retrieveLongsFromQuery(query, "id");
        return memberIds;
    }

    public Member findByEmail(String email) {
        Member member = findOneWhere(String.format("%s.email = '%s'", getEntityName(), email));
        return member;
    }

    public Member findByPhone(String phone) {
        Member member = findOneWhere(String.format("%s.phone = '%s'", getEntityName(), phone));
        return member;
    }

}
