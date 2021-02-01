package com.mthoko.mobile.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.common.BaseServiceImpl;

@Service
public class CredentialsServiceImpl extends BaseServiceImpl<Credentials> implements CredentialsService {

	@Autowired
	private CredentialsRepo credentialsRepo;

	@Override
	public JpaRepository<Credentials, Long> getRepo() {
		return credentialsRepo;
	}

	@Override
	public Credentials findByMemberId(Long memberId) {
		return credentialsRepo.findByMemberId(memberId);
	}
}
