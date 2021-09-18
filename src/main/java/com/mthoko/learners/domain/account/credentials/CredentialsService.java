package com.mthoko.learners.domain.account.credentials;

import com.mthoko.learners.common.service.BaseService;

import java.util.Optional;

public interface CredentialsService extends BaseService<Credentials> {

	Optional<Credentials> findByMemberId(Long memberId);
}
