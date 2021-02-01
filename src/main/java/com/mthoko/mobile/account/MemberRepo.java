package com.mthoko.mobile.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

	Member findByPhone(String phone);

	Member findByEmail(String email);

	int countById(Long id);

}
