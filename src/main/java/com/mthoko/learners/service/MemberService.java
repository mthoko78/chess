package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberService extends BaseService<Member> {

	public Optional<Member> findByEmail(String email);

	public Optional<Member> findByPhone(String phone);

	public List<Member> findByPhoneIn(Set<String> phones);
}
