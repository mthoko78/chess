package com.mthoko.mobile.service;

import com.mthoko.mobile.model.Account;

import java.util.List;
import java.util.Set;

public interface AccountService extends BaseService<Account> {
    Account findByEmail(String email);

    Account findExternalAccountByEmail(String email);

    void register(Account account);

    void attemptToVerify(Account account);

    Account findBySimNo(String simNo);

    Account findRemoteAccountBySimNo(String simNo);

    Account findByMemberId(long memberId);

    List<Account> findVerifiedMatchingAccounts(Account accountForRegistration);

    void loadMatchingEntries(Account accountForRegistration, Account account, Set<String> duplicateEntries);

    List<Account> findExternalMatchingAccounts(Account accountToRegister);

    void registerExternally(Account accountToRegister);

    List<Account> findMatchingAccounts(Account accountToRegister);
}
