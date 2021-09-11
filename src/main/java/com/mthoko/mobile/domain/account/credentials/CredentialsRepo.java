package com.mthoko.mobile.domain.account.credentials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, Long> {

	Optional<Credentials> findByMemberId(Long memberId);
}
