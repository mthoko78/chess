package com.mthoko.learners.domain.account.member;

import com.mthoko.learners.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberServiceImpl extends BaseServiceImpl<Member> implements MemberService {

	@Autowired
	private MemberRepo memberRepo;

	@Override
	public JpaRepository<Member, Long> getRepo() {
		return memberRepo;
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberRepo.findByEmail(email);
	}

	@Override
	public Optional<Member> findByPhone(String phone) {
		return memberRepo.findByPhone(phone);
	}

	@Override
	public List<Member> findByPhoneIn(Set<String> phones) {
		return memberRepo.findByPhoneIn(phones);
	}

}
