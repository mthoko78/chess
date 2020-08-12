package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.Member;

public interface MemberService extends BaseService {
	
	public Member findById(Long id);

	public Member findByEmail(String email);
}
