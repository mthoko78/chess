package com.mthoko.mobile.account;

import com.mthoko.mobile.common.BaseService;

public interface CredentialsService extends BaseService<Credentials> {

	Credentials findByMemberId(Long memberId);
}
