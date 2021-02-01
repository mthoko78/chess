package com.mthoko.mobile.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
public interface AccountRepo extends JpaRepository<Account, Long> {

	@Query("select a from Account a join Member m on a.member.id = a.id where m.email = ?1")
	Account findByEmail(String email);

	@Query("select a from Account a join Member m on a.member.id = a.id where m.phone = ?1")
	Account findByPhone(String phone);
}
