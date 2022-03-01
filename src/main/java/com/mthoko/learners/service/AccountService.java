package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Account;
import com.mthoko.learners.persistence.entity.PhoneVerification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountService extends BaseService<Account> {

    Account findByMemberId(Long memberId);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByPhone(String phone);

    void loadMatchingEntries(Account targetAccount, Account account, Set<String> duplicateEntries);

    List<Account> findMatchingAccounts(Account account);

    Account register(Account account);

    Optional<Account> findByImei(String imei);

    Optional<PhoneVerification> findPhoneVerificationByPhoneNumber(String phoneNumber);

    PhoneVerification savePhoneVerification(PhoneVerification verification);

    String generateVerificationCode();

    PhoneVerification generateAndSavePhoneVerification(String phoneNumber);

    Optional<PhoneVerification> findPhoneVerificationByCode(String verificationCode);
}
