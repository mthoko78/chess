package com.mthoko.mobile.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.Credentials;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, Long> {

	Credentials findByMemberId(Long memberId);
}
