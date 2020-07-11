package com.mthoko.mobile.service.proxy;

import java.util.List;
import java.util.Set;

import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.internal.AccountServiceImpl;
import com.mthoko.mobile.service.internal.BaseServiceImpl;

public class AccountServiceProxy extends BaseServiceImpl<Account> implements AccountService {

    private final AccountServiceImpl service;

    public AccountServiceProxy() {
        service = new AccountServiceImpl();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

    @Override
    public Account findExternalAccountByEmail(String email) {
        boolean openConnection = service.openRemoteConnection();
        Account account = service.findExternalAccountByEmail(email);
        service.closeRemoteConnectionIf(openConnection);
        return account;
    }

    @Override
    public Account findRemoteAccountBySimNo(String simNo) {
        boolean openConnection = service.openRemoteConnection();
        Account account = service.findRemoteAccountBySimNo(simNo);
        service.closeRemoteConnectionIf(openConnection);
        return account;
    }

    @Override
    public void loadMatchingEntries(Account accountForRegistration, Account account, Set<String> duplicateEntries) {
        service.loadMatchingEntries(accountForRegistration, account, duplicateEntries);
    }

    @Override
    public List<Account> findExternalMatchingAccounts(Account account) {
        boolean openConnection = service.openRemoteConnection();
        List<Account> accounts = service.findExternalMatchingAccounts(account);
        service.closeRemoteConnectionIf(openConnection);
        return accounts;
    }

    @Override
    public void registerExternally(Account accountToRegister) {
        boolean connection = service.openRemoteConnection();
        service.registerExternally(accountToRegister);
        service.closeRemoteConnectionIf(connection);
    }

}
