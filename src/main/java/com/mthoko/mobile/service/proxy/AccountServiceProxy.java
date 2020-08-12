package com.mthoko.mobile.service.proxy;

import java.util.List;
import java.util.Set;

import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.internal.AccountServiceImpl;
import com.mthoko.mobile.service.internal.BaseServiceImpl;

public class AccountServiceProxy extends BaseServiceImpl implements AccountService {

	private final AccountServiceImpl service;

	public AccountServiceProxy() {
		service = new AccountServiceImpl();
	}

	@Override
	public BaseResourceRemote<Account> getResource() {
		return service.getResource();
	}

	@Override
	public Account findByEmail(String email) {
		boolean openConnection = service.openConnection();
		Account account = service.findByEmail(email);
		service.closeConnectionIf(openConnection);
		return account;
	}

	@Override
	public Account findBySimNo(String simNo) {
		boolean openConnection = service.openConnection();
		Account account = service.findBySimNo(simNo);
		service.closeConnectionIf(openConnection);
		return account;
	}

	@Override
	public void loadMatchingEntries(Account accountForRegistration, Account account, Set<String> duplicateEntries) {
		service.loadMatchingEntries(accountForRegistration, account, duplicateEntries);
	}

	@Override
	public List<Account> findMatchingAccounts(Account account) {
		boolean openConnection = service.openConnection();
		List<Account> accounts = service.findMatchingAccounts(account);
		service.closeConnectionIf(openConnection);
		return accounts;
	}

	@Override
	public void register(Account accountToRegister) {
		boolean connection = service.openConnection();
		service.register(accountToRegister);
		service.closeConnectionIf(connection);
	}

}
