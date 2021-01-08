package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.Credentials;

public interface CredentialsService extends BaseService<Credentials> {

	Credentials findByMemberId(Long memberId);
}
