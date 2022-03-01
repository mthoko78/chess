package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findByMember_Email(String email);

    Optional<Account> findByMember_Phone(String phone);

    @Query("select a from Account a join Device d on a.member.id = d.memberId where d.imei = ?1")
    Optional<Account> findByImei(String imei);

    Account findByMember_Id(Long memberId);
}
