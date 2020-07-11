package com.mthoko.mobile.service.proxy;

import android.content.Context;

import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.internal.AccountServiceImpl;
import com.mthoko.mobile.service.internal.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccountServiceProxy extends BaseServiceImpl<Account> implements AccountService {

    private final AccountServiceImpl service;

    public AccountServiceProxy(Context context) {
        service = new AccountServiceImpl(context);
    }

    @Override
    public BaseResource getResource() {
        return service.getResource();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return service.getRemoteResource();
    }

    @Override
    public void setContext(Context context) {
        service.setContext(context);
    }

    @Override
    public Account findByEmail(String email) {
        boolean openConnection = service.openConnection();
        Account account = service.findByEmail(email);
        service.closeConnectionIf(openConnection);
        return account;
    }

    @Override
    public Account findExternalAccountByEmail(String email) {
        boolean openConnection = service.openRemoteConnection();
        Account account = service.findExternalAccountByEmail(email);
        service.closeRemoteConnectionIf(openConnection);
        return account;
    }

    @Override
    public void register(Account account) {
        boolean openConnection = service.openConnection();
        boolean inTransaction = service.beginTransaction();
        service.register(account);
        service.endTransactionIf(inTransaction);
        service.closeConnectionIf(openConnection);
    }

    @Override
    public void attemptToVerify(Account account) {
        boolean openConnection = service.openConnection();
        boolean inTransaction = service.beginTransaction();
        boolean openRemoteConnection = service.openRemoteConnection();
        boolean inRemoteTransaction = service.beginRemoteTransaction();
        service.attemptToVerify(account);
        service.endRemoteTransactionIf(inRemoteTransaction);
        service.closeRemoteConnectionIf(openRemoteConnection);
        service.endTransactionIf(inTransaction);
        service.closeConnectionIf(openConnection);
    }

    @Override
    public Account findBySimNo(String simNo) {
        boolean openConnection = service.openConnection();
        Account account = service.findBySimNo(simNo);
        service.closeConnectionIf(openConnection);
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
    public Account findByMemberId(long memberId) {
        boolean openConnection = service.openConnection();
        Account account = service.findByMemberId(memberId);
        service.closeConnectionIf(openConnection);
        return account;
    }

    @Override
    public List<Account> findVerifiedMatchingAccounts(Account account) {
        boolean openConnection = service.openConnection();
        ArrayList<Account> accounts = service.findVerifiedMatchingAccounts(account);
        service.closeConnectionIf(openConnection);
        return accounts;
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

    @Override
    public List<Account> findMatchingAccounts(Account account) {
        boolean openConnection = service.openConnection();
        ArrayList<Account> accounts = service.findMatchingAccounts(account);
        service.closeConnectionIf(openConnection);
        return accounts;
    }
}
