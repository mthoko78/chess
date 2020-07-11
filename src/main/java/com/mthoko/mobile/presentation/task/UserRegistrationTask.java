package com.mthoko.mobile.presentation.task;

import android.content.Intent;
import android.os.AsyncTask;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.util.MyConstants;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.presentation.activity.LoginActivity;
import com.mthoko.mobile.presentation.activity.RegisterActivity;
import com.mthoko.mobile.presentation.controller.RegisterController;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserRegistrationTask extends AsyncTask<Object, Object, Integer> implements MyConstants {

    private final String name;
    private final String surname;
    private final String phone;
    private final String email;
    private final String password;
    private final RegisterActivity activity;
    private final boolean deviceOwner;
    private final RegisterController registerController;
    private final Account accountForRegistration;
    private ApplicationException exception;

    public UserRegistrationTask(RegisterActivity activity, String name, String surname, String phone, String email, String password, boolean deviceOwner) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.deviceOwner = deviceOwner;
        this.activity = activity;
        this.registerController = new RegisterController(activity);
        this.accountForRegistration = getAccountForRegistration();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.setTask(this);
    }

    @Override
    protected Integer doInBackground(Object... params) {
        try {
            return registerController.attemptToRegister(accountForRegistration, deviceOwner);
        } catch (ApplicationException e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Integer status) {
        activity.showProgress(false);
        if (exception != null) {
            handleRegistrationFailure(exception);
        } else {
            processRegistrationSuccess(status);
        }
        activity.setTask(null);
    }

    private void processRegistrationSuccess(Integer status) {
        switch (status) {
            case STATUS_SUCCESS_VERIFIED:
            case STATUS_SUCCESS_UNVERIFIED: {
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
                break;
            }
        }
    }

    private void handleRegistrationFailure(ApplicationException exception) {
        String status = "Status";
        switch (exception.getErrorCode()) {
            case STATUS_FAIL_SIM_ALREADY_REGISTERED: {
                activity.showMessage(status, "The sim card is already registered");
                break;
            }
            case STATUS_FAIL_DUP_ENTRIES: {
                String message = "Some of the information you provided belongs to an existing user";
                try {
                    Set<String> duplicateEntries = getDuplicateEntries();
                    message += ": " + duplicateEntries;
                } catch (ApplicationException e) {
                    message += ": " + e.getMessage();
                }
                activity.showMessage(status, message);
                break;
            }
            case STATUS_FAIL_DEVICE_ALREADY_ALLOCATED: {
                activity.showMessage(status, "This device has already been allocated to someone" +
                        " please send an email to: mthoko78@outlook.com for any enquiries.");
                break;
            }
        }
    }

    private Set<String> getDuplicateEntries() {
        Set<String> duplicateEntries = new HashSet<>();
        AccountService accountService = ServiceFactory.getAccountService(this.activity);
        List<Account> matchingAccounts = accountService.findVerifiedMatchingAccounts(accountForRegistration);
        for (Account account : matchingAccounts) {
            accountService.loadMatchingEntries(accountForRegistration, account, duplicateEntries);
        }
        return duplicateEntries;
    }

    @Override
    protected void onCancelled() {
        activity.setTask(null);
        activity.showProgress(false);
    }

    private Account getAccountForRegistration() {
        Member member = new Member();
        member.setName(name);
        member.setSurname(surname);
        member.setPhone(phone);
        member.setEmail(email);

        Credentials credentials = new Credentials();
        credentials.setPassword(password);

        SimCard simCard = new SimCard();
        simCard.setPhone(phone);
        simCard.setSimNo(registerController.getCurrentSimNo());

        Account account = new Account();
        account.setCredentials(credentials);
        account.setSimCards(Arrays.asList(simCard));
        account.setMember(member);
        if (deviceOwner) {
            Device device = new Device();
            device.setImei(registerController.getService().getImei());
            device.setName(registerController.getService().getDeviceName());
            device.setDateRegistered(new Date());
            account.setDevices(Arrays.asList(device));
        }
        return account;
    }
}
