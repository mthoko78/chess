package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Credentials;

import java.util.Optional;

public interface CredentialsService extends BaseService<Credentials> {

	Optional<Credentials> findByMemberId(Long memberId);
}
