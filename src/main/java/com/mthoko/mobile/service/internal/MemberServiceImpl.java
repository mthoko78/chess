package com.mthoko.mobile.service.internal;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.repo.MemberRepo;
import com.mthoko.mobile.service.MemberService;

@Service
public class MemberServiceImpl extends BaseServiceImpl<Member> implements MemberService {

	@Autowired
	private MemberRepo memberRepo;

	@Override
	public JpaRepository<Member, Long> getRepo() {
		return memberRepo;
	}

	@Override
	public Member findByEmail(String email) {
		return memberRepo.findByEmail(email);
	}

	@Override
	public Member findBySimNo(String simNo) {
		return memberRepo.findBySimNo(simNo);
	}

	@Override
	public Member findByPhone(String phone) {
		return memberRepo.findByPhone(phone);
	}

	@Override
	public List<Member> findByPhoneIn(Set<String> phones) {
		return memberRepo.findByPhoneIn(phones);
	}

}
