package com.mthoko.mobile.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, Long> {

	Credentials findByMemberId(Long memberId);
}
