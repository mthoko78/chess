package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.Member;

public class MemberResourceApi extends BaseResourceApi<Member> {

    public MemberResourceApi(Context context) {
        super(context, Member.class);
    }

}
