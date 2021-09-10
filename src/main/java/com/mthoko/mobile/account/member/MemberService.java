package com.mthoko.mobile.account.member;

import com.mthoko.mobile.common.BaseService;

public interface MemberService extends BaseService<Member> {

	Member findByEmail(String email);

	Member findByPhone(String phone);
}
