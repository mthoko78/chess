package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.exception.ErrorCode;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.resource.internal.AccountResource;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.AccountResourceRemote;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

    private final AccountResource accountResource;

    private final AccountResourceRemote accountResourceRemote;


    public AccountServiceImpl(Context context) {
        accountResource = new AccountResource(context, new DatabaseWrapper());
        accountResourceRemote = new AccountResourceRemote(context, new ConnectionWrapper(null));
    }

    public void registerExternally(Account account) { // this method must be called within AsyncClass since it makes async http requests
        accountResourceRemote.register(account);
    }

    public ArrayList<Account> findVerifiedMatchingAccounts(Account accountToRegister) {
        ArrayList<Account> matchingAccounts = findMatchingAccounts(accountToRegister);
        ArrayList<Account> unverifiedMatchingAccounts = new ArrayList<>();
        for (Account account : matchingAccounts) {
            if (!account.isValid()) {
                unverifiedMatchingAccounts.add(account);
            }
        }
        matchingAccounts.removeAll(unverifiedMatchingAccounts);
        return matchingAccounts;
    }

    public ArrayList<Account> findMatchingAccounts(Account account) {
        Set<Long> memberIds = new LinkedHashSet<>();
        if (!account.getSimCards().isEmpty()) {
            memberIds.addAll(accountResource.simCardResource.findMemberIdsForSimCards(new ArrayList<>(account.getSimCards())));
        }
        String email = account.getEmail();
        String phone = account.getMember().getPhone();
        memberIds.addAll(accountResource.memberResource.findMemberIdsByEmailOrPhone(email, phone));
        ArrayList<Account> accounts = new ArrayList<>();
        for (long memberId : memberIds) {
            Account matchingAccount = findByMemberId(memberId);
            accounts.add(matchingAccount);
        }
        return accounts;
    }

    public Account findByMemberId(long memberId) {
        return accountResource.findByMemberId(memberId);
    }

    public List<Account> findExternalMatchingAccounts(Account targetAccount) {
        List<Account> matchingAccounts = accountResourceRemote.findMatchingAccounts(targetAccount);
        return matchingAccounts;
    }

    public void loadMatchingEntries(Account first, Account second, Set<String> duplicateEntries) {
        if (first.getEmail().equals(second.getEmail()))
            duplicateEntries.add("email");
        if (first.getMember().getPhone().equals(second.getMember().getPhone()))
            duplicateEntries.add("phone");
        for (SimCard firstSim : first.getSimCards()) {
            for (SimCard secondSim : second.getSimCards()) {
                if (firstSim.getSimNo().equals(secondSim.getSimNo()))
                    duplicateEntries.add("sim number : " + firstSim.getSimNo());
                if (firstSim.getPhone().equals(secondSim.getPhone()))
                    duplicateEntries.add("phone : " + firstSim.getPhone());
            }
        }
    }

    public Account findBySimNo(String simNo) {
        SimCard simCard = accountResource.simCardResource.findBySimNo(simNo);
        Account account = null;
        if (simCard != null) {
            account = findByMemberId(simCard.getMemberId());
        }

        return account;
    }

    public Account findRemoteAccountBySimNo(String simNo) {
        Account account = accountResourceRemote.findBySimNo(simNo);
        return account;
    }

    public void register(Account account) {
        long memberId = accountResource.memberResource.save(account.getMember());
        account.getCredentials().setMemberId(memberId);
        accountResource.credentialsResource.save(account.getCredentials());
        for (SimCard simCard : account.getSimCards()) {
            simCard.setMemberId(memberId);
        }
        for (Device device : account.getDevices()) {
            device.setMemberId(memberId);
        }
        accountResource.simCardResource.saveAll(account.getSimCards());
        accountResource.deviceResource.saveAll(account.getDevices());
    }

    @Override
    public void update(Account account) {
        boolean transaction = accountResource.beginTransaction();
        accountResource.memberResource.update(account.getMember());
        accountResource.credentialsResource.update(account.getCredentials());
        accountResource.simCardResource.updateAll(account.getSimCards());
        accountResource.deviceResource.updateAll(account.getDevices());
        accountResource.endTransactionIf(transaction);
    }

    @Override
    public void setContext(Context context) {
        accountResource.setContext(context);
        accountResource.simCardResource.setContext(context);
        accountResource.credentialsResource.setContext(context);
        accountResource.deviceResource.setContext(context);
        accountResource.memberResource.setContext(context);
    }

    @Override
    public BaseResource getResource() {
        return accountResource;
    }

    @Override
    public Context getContext() {
        return accountResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return accountResourceRemote;
    }

    public Account findByEmail(String email) {
        Member member = accountResource.memberResource.findByEmail(email);
        Account account = null;
        if (member != null) {
            account = findByMember(member);
        }

        return account;
    }

    public void attemptToVerify(Account account) {
        if (!accountResourceRemote.isConnectionAvailable()) {
            throw new ApplicationException(ErrorCode.NO_INTERNET_CONNECTION);
        }
        String email = account.getMember().getEmail();
        Map<String, Long> verification = accountResourceRemote.retrieveVerificationByEmail(email);
        if (!verification.isEmpty()) {
            verify(account, verification);
        } else {
            List<Account> matchingAccounts = accountResourceRemote.findMatchingAccounts(account);
            if (matchingAccounts.isEmpty()) { // No conflicts, this account can be safely registered externally && verified
                accountResourceRemote.register(account);
            } else {
                handleDuplicates(account, matchingAccounts);
            }
        }
    }

    private void handleDuplicates(Account account, List<Account> matchingAccounts) {
        SimCard primarySimCard = account.getPrimarySimCard();
        boolean phoneAlreadyInUse = false;
        boolean emailAlreadyInUse = false;
        // this is the only sim card for this account since unverified accounts cannot have more than one sim card
        for (Account matchingAccount : matchingAccounts) {
            for (SimCard simCard : matchingAccount.getSimCards()) {
                if (!phoneAlreadyInUse && simCard.getPhone().equals(primarySimCard.getPhone())) {
                    phoneAlreadyInUse = true;
                }
            }
            if (!emailAlreadyInUse && account.getEmail().equals(matchingAccount.getEmail())) {
                emailAlreadyInUse = true;
            }
            if (!phoneAlreadyInUse && account.getMember().getPhone().equals(matchingAccount.getMember().getPhone())) {
                phoneAlreadyInUse = true;
            }
        }
        ErrorCode errorCode;
        if (emailAlreadyInUse && phoneAlreadyInUse) {
            errorCode = ErrorCode.EMAIL_AND_PHONE_ALREADY_IN_USE;
        } else if (phoneAlreadyInUse) {
            errorCode = ErrorCode.PHONE_ALREADY_IN_USE;
        } else {
            errorCode = ErrorCode.EMAIL_ALREADY_IN_USE;
        }
        throw new ApplicationException(errorCode);
    }

    public void verify(Account account, Map<String, Long> verification) {
        SimCard primarySimCard = account.getPrimarySimCard();
        Device primaryDevice = account.getPrimaryDevice();
        String memberUniqueIdentifier = account.getMember().getUniqueIdentifier();
        String credentialsUniqueIdentifier = account.getCredentials().getUniqueIdentifier();
        String simUniqueIdentifier = primarySimCard.getUniqueIdentifier();
        String devUniqueIdentifier = primaryDevice.getUniqueIdentifier();
        if (verification.containsKey(memberUniqueIdentifier)
                && verification.containsKey(credentialsUniqueIdentifier)
                && verification.containsKey(simUniqueIdentifier)
                && verification.containsKey(devUniqueIdentifier)) {

            boolean transaction = accountResource.beginTransaction();
            accountResource.memberResource.verify(account.getMember(), verification);
            accountResource.credentialsResource.verify(account.getCredentials(), verification);
            accountResource.simCardResource.verifyAll(account.getSimCards(), verification);
            accountResource.deviceResource.verifyAll(account.getDevices(), verification);
            accountResource.endTransactionIf(transaction);
        } else {
            accountResource.rollBack();
            throw new ApplicationException("Failed to verify account due to duplicate information");
        }
    }

    public Account findExternalAccountByEmail(String email) {
        Account account = accountResourceRemote.findByEmail(email);
        return account;
    }

    private Account findByMember(Member member) {
        Account account = new Account();
        account.setMember(member);

        account.setSimCards(accountResource.simCardResource.findByMemberId(member.getId()));
        account.setCredentials(accountResource.credentialsResource.findByMemberId(member.getId()));
        account.setDevices(accountResource.deviceResource.findByMemberId(member.getId()));
        return account;
    }
}
