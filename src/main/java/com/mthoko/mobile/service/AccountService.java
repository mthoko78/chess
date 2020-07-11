package com.mthoko.mobile.service;

import com.mthoko.mobile.model.Account;

import java.util.List;
import java.util.Set;

public interface AccountService extends BaseService<Account> {

	Account findExternalAccountByEmail(String email);

    Account findRemoteAccountBySimNo(String simNo);

    void loadMatchingEntries(Account accountForRegistration, Account account, Set<String> duplicateEntries);

    List<Account> findExternalMatchingAccounts(Account accountToRegister);

    void registerExternally(Account accountToRegister);

}
