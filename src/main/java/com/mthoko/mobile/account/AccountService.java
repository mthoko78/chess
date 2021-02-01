package com.mthoko.mobile.account;

import java.util.List;
import java.util.Set;

import com.mthoko.mobile.common.BaseService;

public interface AccountService extends BaseService<Account> {

	Account findByEmail(String email);

	Account findByPhone(String phone);

	void loadMatchingEntries(Account targetAccount, Account account, Set<String> duplicateEntries);

	List<Account> findMatchingAccounts(Account account);

	Account register(Account account);

}
