package com.mthoko.mobile.service.internal;

import java.util.List;
import java.util.Set;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.exception.ErrorCode;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.resource.remote.AccountResourceRemote;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

    private final AccountResourceRemote accountResourceRemote;

    public AccountServiceImpl() {
        accountResourceRemote = new AccountResourceRemote(new ConnectionWrapper(null));
    }

    public void registerExternally(Account account) { // this method must be called within AsyncClass since it makes async http requests
        accountResourceRemote.register(account);
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

    public Account findRemoteAccountBySimNo(String simNo) {
        Account account = accountResourceRemote.findBySimNo(simNo);
        return account;
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return accountResourceRemote;
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

    public Account findExternalAccountByEmail(String email) {
        Account account = accountResourceRemote.findByEmail(email);
        return account;
    }

}
