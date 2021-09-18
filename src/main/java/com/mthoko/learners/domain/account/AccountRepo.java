package com.mthoko.learners.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    @Query("select a from Account a join Member m on a.member.id = a.id where m.email = ?1")
    Optional<Account> findByEmail(String email);

    @Query("select a from Account a join Member m on a.member.id = a.id where m.phone = ?1")
    Optional<Account> findByPhone(String phone);

    @Query("select a from Account a join Device d on a.member.id = d.memberId where d.imei = ?1")
    Optional<Account> findByImei(String imei);

    @Query("select a from Account a join Member m on a.member.id = a.id where m.id = ?1")
    Account findByMemberId(Long memberId);
}
