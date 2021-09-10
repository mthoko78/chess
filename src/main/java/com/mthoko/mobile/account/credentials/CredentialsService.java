package com.mthoko.mobile.account.credentials;

import com.mthoko.mobile.common.BaseService;

public interface CredentialsService extends BaseService<Credentials> {

	Credentials findByMemberId(Long memberId);
}
