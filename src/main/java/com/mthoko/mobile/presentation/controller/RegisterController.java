package com.mthoko.mobile.presentation.controller;

import android.content.Context;
import android.text.TextUtils;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.exception.ErrorCode;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.util.List;
import java.util.regex.Pattern;

public class RegisterController extends BaseController {

    private final AccountService accountService;
    private final DeviceService deviceService;

    public RegisterController(Context context) {
        accountService = ServiceFactory.getAccountService(context);
        deviceService = ServiceFactory.getDeviceService(context);
    }

    public void validatePasswordFormat(String password) throws ApplicationException {
        ErrorCode errorCode = ErrorCode.INVALID_PASSWORD;
        if (TextUtils.isEmpty(password)) {
            throw new ApplicationException("This field is required", errorCode);
        } else if (password.length() < 4) {
            throw new ApplicationException("password too short", errorCode);
        } else if (!isPasswordValid(password)) {
            throw new ApplicationException("The password must contain at least one letter / phone", errorCode);
        }
    }

    public void validateEmailFormat(String email) throws ApplicationException {
        ErrorCode errorCode = ErrorCode.INVALID_EMAIL;
        if (TextUtils.isEmpty(email)) {
            throw new ApplicationException("This field is required", errorCode);
        } else if (!isEmailValid(email)) {
            throw new ApplicationException("This email address is invalid", errorCode);
        }
    }

    public void validatePhone(String phone) throws ApplicationException {
        ErrorCode errorCode = ErrorCode.INVALID_PHONE;
        if (TextUtils.isEmpty(phone)) {
            throw new ApplicationException("This field is required", errorCode);
        } else if (!TextUtils.isDigitsOnly(phone.replaceAll("[\\+\\s]", ""))) {
            throw new ApplicationException("This phone is invalid", errorCode);
        }
    }

    public void validateSurname(String surname) throws ApplicationException {
        validateWord(surname, "surname", ErrorCode.INVALID_SURNAME);
    }

    public void validateName(String name) throws ApplicationException {
        validateWord(name, "name", ErrorCode.INVALID_NAME);
    }

    private void validateWord(String word, String type, ErrorCode errorCode) {
        if (TextUtils.isEmpty(word)) {
            throw new ApplicationException("This field is required", errorCode);
        } else if (!word.replaceAll("\\w", "").isEmpty()) {
            throw new ApplicationException("This " + type + "is invalid", errorCode);
        }
    }

    private static boolean isEmailValid(String email) {
        return Pattern.matches("(\\w+)(\\.\\w+)*@(\\w+)\\.(\\w+)(\\.(\\w+))*", email);
    }

    private static boolean isPasswordValid(String password) {
        return Pattern.matches("\\w+", password);
    }

    @Override
    public BaseService getService() {
        return accountService;
    }

    public Integer attemptToRegister(Account accountToRegister, boolean deviceOwner) {
        if (findAccountBySimNo(getCurrentSimNo()) != null) {
            // the current sim card has already been registered, can only be registered once
            throw new ApplicationException(ErrorCode.STATUS_FAIL_SIM_ALREADY_REGISTERED);
        }

        if (!accountService.isConnectionAvailable()) {
            processOfflineRegistration(accountToRegister, deviceOwner);
            return STATUS_SUCCESS_UNVERIFIED;
        }
        processRegistrationWithConnection(accountToRegister, deviceOwner);
        return STATUS_SUCCESS_VERIFIED;
    }

    private void processRegistrationWithConnection(Account accountToRegister, boolean deviceOwner) {
        List<Account> matchingAccounts = accountService.findExternalMatchingAccounts(accountToRegister);
        if (!matchingAccounts.isEmpty()) {
            // cant register with this account, matching accounts loaded from external server, need to register these internally
            throw new ApplicationException(ErrorCode.STATUS_FAIL_DUP_ENTRIES);
        }
        if (deviceOwner) {
            Device existingDevice = deviceService.findRemoteDeviceByImei(accountToRegister.getPrimaryDevice().getImei());
            if (existingDevice != null) {
                throw new ApplicationException(ErrorCode.STATUS_FAIL_DEVICE_ALREADY_ALLOCATED);
            }
        }
        accountService.registerExternally(accountToRegister);
        accountService.register(accountToRegister);
    }

    private void processOfflineRegistration(Account accountToRegister, boolean deviceOwner) {
        List<Account> matchingAccounts = accountService.findMatchingAccounts(accountToRegister);
        if (!matchingAccounts.isEmpty()) {
            // cant register with this account, matching accounts loaded from external server, need to register these internally
            throw new ApplicationException(ErrorCode.STATUS_FAIL_DUP_ENTRIES);
        }
        if (deviceOwner) {
            Device existingDevice = deviceService.findByImei(accountToRegister.getPrimaryDevice().getImei());
            if (existingDevice != null) {
                throw new ApplicationException(ErrorCode.STATUS_FAIL_DEVICE_ALREADY_ALLOCATED);
            }
        }
        accountService.register(accountToRegister);
    }

    public Account findAccountBySimNo(String simNo) {
        return accountService.findBySimNo(simNo);
    }
}