package com.mthoko.mobile.domain.account.credentials;

import com.mthoko.mobile.common.service.BaseService;

import java.util.Optional;

public interface CredentialsService extends BaseService<Credentials> {

	Optional<Credentials> findByMemberId(Long memberId);
}
