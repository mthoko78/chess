package com.mthoko.mobile.account;

import com.mthoko.mobile.common.BaseService;

public interface MemberService extends BaseService<Member> {

	public Member findByEmail(String email);

	public Member findByPhone(String phone);
}
