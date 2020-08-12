package com.mthoko.mobile.service;

import com.mthoko.mobile.model.Account;

import java.util.List;
import java.util.Set;

public interface AccountService extends BaseService {

	Account findByEmail(String email);

    Account findBySimNo(String simNo);

    void loadMatchingEntries(Account targetAccount, Account account, Set<String> duplicateEntries);

    List<Account> findMatchingAccounts(Account account);

    void register(Account account);

}
