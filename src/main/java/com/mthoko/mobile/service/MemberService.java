package com.mthoko.mobile.service;

import java.util.List;
import java.util.Set;

import com.mthoko.mobile.entity.Member;

public interface MemberService extends BaseService<Member> {

	public Member findByEmail(String email);

	public Member findBySimNo(String simNo);

	public Member findByPhone(String phone);

	public List<Member> findByPhoneIn(Set<String> phones);
}
