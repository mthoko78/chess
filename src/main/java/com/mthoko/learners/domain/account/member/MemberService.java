package com.mthoko.learners.domain.account.member;

import com.mthoko.learners.common.service.BaseService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberService extends BaseService<Member> {

	public Optional<Member> findByEmail(String email);

	public Optional<Member> findByPhone(String phone);

	public List<Member> findByPhoneIn(Set<String> phones);
}
