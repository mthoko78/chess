package com.mthoko.mobile.domain.account;

import com.mthoko.mobile.common.BaseService;

import java.util.List;
import java.util.Set;

public interface AccountService extends BaseService<Account> {

	Account findByEmail(String email);

	Account findByPhone(String phone);

	void loadMatchingEntries(Account targetAccount, Account account, Set<String> duplicateEntries);

	List<Account> findMatchingAccounts(Account account);

	Account register(Account account);

}
