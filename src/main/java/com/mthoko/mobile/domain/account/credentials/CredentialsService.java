package com.mthoko.mobile.domain.account.credentials;

import com.mthoko.mobile.common.BaseService;

public interface CredentialsService extends BaseService<Credentials> {

	Credentials findByMemberId(Long memberId);
}
