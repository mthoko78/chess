package com.mthoko.mobile.domain.account.member;

import com.mthoko.mobile.common.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
	public Member findByPhone(String phone) {
		return memberRepo.findByPhone(phone);
	}

}
