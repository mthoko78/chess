package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Credentials;
import com.mthoko.learners.persistence.repository.CredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsServiceImpl extends BaseServiceImpl<Credentials> implements CredentialsService {

	@Autowired
	private CredentialsRepo credentialsRepo;

	@Override
	public JpaRepository<Credentials, Long> getRepo() {
		return credentialsRepo;
	}

	@Override
	public Optional<Credentials> findByMemberId(Long memberId) {
		return credentialsRepo.findByMemberId(memberId);
	}
}
