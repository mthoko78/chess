package com.mthoko.mobile.presentation.controller;

import android.content.Context;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.exception.ErrorCode;
import com.mthoko.mobile.model.Account;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.mthoko.mobile.exception.ApplicationException.GENERIC_ERROR_MESSAGE;

public class LoginController extends BaseController {

    private final AccountService accountService;

    private final DeviceService deviceService;

    private final DevContactService devContactService;

    private final SimContactService simContactService;

    private final SmsService smsService;

    public LoginController(Context context) {
        accountService = ServiceFactory.getAccountService(context);
        deviceService = ServiceFactory.getDeviceService(context);
        devContactService = ServiceFactory.getContactService(context);
        simContactService = ServiceFactory.getSimContactService(context);
        smsService = ServiceFactory.getSmsService(context);
    }

    public String processAccountStatus(Account account, String simNo, Integer status) {
        String statusDescription = getMessages(account, simNo).get(status);
        return statusDescription;
    }

    public Account authenticateLogin(String email, String password) {
        Account account = retrieveAccountForLogin(email);
        if (!account.getPassword().equals(password)) {
            throw new ApplicationException(ErrorCode.INCORRECT_PASSWORD);
        }
        return account;
    }

    private Account retrieveAccountForLogin(String email) {
        Account account = accountService.findByEmail(email);
        if (account != null) {
            if (!account.isVerified() && accountService.isConnectionAvailable()) {
                accountService.attemptToVerify(account);
            }
            return account;
        }
        if (!accountService.isConnectionAvailable()) {
            throw new ApplicationException(ErrorCode.NO_INTERNET_CONNECTION);
        }
        account = accountService.findExternalAccountByEmail(email);
        if (account == null) {
            throw new ApplicationException(ErrorCode.ACCOUNT_DOES_NOT_EXIST);
        }
        accountService.register(account);
        return account;
    }

    public Account processPreLogin() {
        String simNo = accountService.getCurrentSimNo();
        if (simNo == null) {
            String noSimCardDetected = "No sim card detected";
            accountService.showNotification(noSimCardDetected);
            throw new ApplicationException(noSimCardDetected, ErrorCode.NO_SIM_CARD_DETECTED);
        }
        Account accountForCurrSim = accountService.findBySimNo(simNo);
        if (accountForCurrSim == null) {
            if (!accountService.isConnectionAvailable()) {
                throw new ApplicationException(ErrorCode.NO_INTERNET_CONNECTION);
            }
            if ((accountForCurrSim = accountService.findRemoteAccountBySimNo(simNo)) != null) {
                /**
                 * TODO:: find matching accounts and resolve inconsistencies if any
                 */
                accountService.register(accountForCurrSim);
            }
        }
        if (accountForCurrSim != null) {
            processFoundAccount(simNo, accountForCurrSim);
        }
        return accountForCurrSim;
    }

    private void processFoundAccount(String simNo, Account accountForCurrSim) {
        String imei = accountService.getImei();
        SimCard simCard = accountForCurrSim.getSimCardBySimNo(simNo);
        Device device = accountForCurrSim.getDeviceByImei(imei);
        integrateAccountInternally(simCard, device);
        if (!accountForCurrSim.isVerified()) {
            try {
                accountService.attemptToVerify(accountForCurrSim);
            } catch (ApplicationException e) {
                if (!ErrorCode.NO_INTERNET_CONNECTION.equals(e.getErrorCode())) {
                    throw e;
                }
            }
        }
    }

    private void integrateAccountExternally(Account accountForCurrSim, SimCard simCard, Device device) {
        if (!accountForCurrSim.isVerified()) {
            accountService.attemptToVerify(accountForCurrSim);
        }
        simContactService.integrateContactsExternally(simCard);
        devContactService.integrateContactsExternally(device);
        smsService.integrateMessagesExternally(simCard);
    }

    private void integrateAccountInternally(SimCard simCard, Device device) {
        simContactService.integrateContactsInternally(simCard);
        devContactService.integrateContactsInternally(device);
        smsService.integrateMessagesInternally(simCard);
    }

    private Account retrieveAccount(String simNo, boolean connectionAvailable) {
        Account account = accountService.findBySimNo(simNo);
        if (account == null && connectionAvailable) {
            if ((account = accountService.findRemoteAccountBySimNo(simNo)) != null) {
                accountService.register(account);
            }
        }
        return account;
    }

    public Account findAccountBySimNo(String simNumber) {
        return accountService.findBySimNo(simNumber);
    }

    @Override
    public BaseService getService() {
        return accountService;
    }

    public String getImei() {
        return deviceService.getImei();
    }

    public Account findAccountByMemberId(long memberId) {
        return accountService.findByMemberId(memberId);
    }

    public static Map<Integer, String> getMessages(Account account, String simNo) {
        Map<Integer, String> messages = new LinkedHashMap<>();
        messages.put(STATUS_OFFLINE_NO_INTERNAL_ACCOUNT, "Unable to integrate this account due to internet connection failure. " +
                "Please ensure you have reliable internet connection / stable WI-FI connection and restart the app.");

        messages.put(STATUS_ACCOUNT_DOES_NOT_EXIST, "No account has been linked with current sim card inserted on this device. " +
                "You may use registration option to register an account with this sim card. " +
                "Alternatively you may attempt to login to any other account that was previously registered and verified.");

        messages.put(STATUS_OFFLINE_ACCOUNT_NOT_VERIFIED, "The account linked with the current sim card in this device has NOT BEEN VERIFIED " +
                "since it was registered offline. You ignore verifying this account at own risk and please be aware " +
                "that as a result some features cannot be accessed and back up is impossible. To verify/amend this account " +
                "please ensure you have reliable internet connection / stable WI-FI connection and restart the app");

        messages.put(STATUS_NO_INTERNET_CONNECTION, "Please ensure you have  stable internet connection and restart the app.");

        messages.put(STATUS_NATIVE_SUCCESS_VERIFIED, "Welcome back");

        messages.put(STATUS_NATIVE_SUCCESS_UNVERIFIED, "Welcome. Please note that your account has not been verified due to internet connection failure. " +
                "To have it verified please ensure you have a stable internet connection and restart the app.");

        messages.put(STATUS_FOREIGN_SUCCESS_VERIFIED, "Welcome. You do not own either the sim card or device therefore some sim/device information" +
                " will not be available to you");

        messages.put(STATUS_FOREIGN_SUCCESS_UNVERIFIED, "You account has not been verified, please verify your account as soon as possible " +
                "to ensure integrity of your information");

        messages.put(STATUS_FOREIGN_INCORRECT_PASSWORD_VERIFIED, "INCORRECT PASSWORD for foreign unverified account");

        messages.put(STATUS_NATIVE_INCORRECT_PASSWORD, "INCORRECT PASSWORD for native account");

        messages.put(STATUS_OFFLINE_NO_INTERNAL_ACCOUNT, "No account found with the specified email. Please ensure you are connected to internet to " +
                "search for account from external servers");

        messages.put(STATUS_ACCOUNT_DOES_NOT_EXIST, "No account found with the specified email");


        messages.put(STATUS_FOREIGN_INCORRECT_PASSWORD_VERIFIED_OVERWRITTEN, "This account needs attention, some inconsistencies have been detected and resolved automatically");

        messages.put(STATUS_FOREIGN_SUCCESS_VERIFIED_OVERWRITTEN, "This account needs attention, some inconsistencies have been detected and resolved automatically");

        messages.put(STATUS_FOREIGN_INCORRECT_PASSWORD_UNVERIFIED, "Incorrect password");

        messages.put(STATUS_FAIL_INTERNAL_ACCOUNT_NEEDS_ATTENTION, "This account needs attention, some inconsistencies have been detected");

        messages.put(STATUS_UNKNOWN_ERROR, GENERIC_ERROR_MESSAGE);

        if (simNo != null) {

            messages.put(STATUS_REGISTERED_INT_FIRST_TIME, "An account that was previously registered and verified with this sim card has been loaded into this device. " +
                    "Please note that you may not register an account with this sim card again as you can only link sim card into " +
                    "ONE account only. if you don't recognize this account please send an email to mthoko78@outlook.com with ref. no: " +
                    simNo + "."); // LOADED INTERNALLY

            messages.put(STATUS_OVERWRITTEN_BY_EX_ACCOUNT, "Some changes have occurred into the account you have been using on this device because " +
                    "an account that was registered and verified earlier with this sim card has been loaded into this device. " +
                    "Please note that you may not register an account with this sim card again as you can only link sim card with " +
                    "ONE account. For any questions/queries regarding this activity or if you don't recognize this account please " +
                    "send an email to mthoko78@outlook.com with ref. no: " +
                    simNo + "."); // LOADED INTERNALLY
        }

        if (account != null) {
            messages.put(STATUS_REGISTERED_EXT_FIRST_TIME, "Welcome " + account.getMember().getName() + " we are pleased to inform you that your account " +
                    "is FULLY REGISTERED verified with System Monitor external server. Your information is safe, you now have full benefits of this app. " +
                    "Stay connected to have full integrity of your information.");

            messages.put(STATUS_VERIFIED_FIRST_TIME, "Welcome " + account.getMember().getName() + " we are pleased to inform you that your account " +
                    "is now VERIFIED, you now have full benefits of this app. Stay connected to have full integrity of your information.");

            messages.put(STATUS_ERR_EMAIL_PHONE_ALREADY_IN_USE, "Welcome " + account.getMember().getName() + ". Please be informed that the email: " +
                    account.getEmail() + " and phone " + account.getMember().getPhone() + " is already in use " +
                    "with a verified account that belongs to some one else. Since your account was registered offline this inconsistency " +
                    "could not be anticipated for until you are connected to the internet. As a result you will need to change these " +
                    "details. We are sorry for any inconvenience caused. \n\nWould you like to change these details now?");

            messages.put(STATUS_ERR_PHONE_ALREADY_IN_USE, "Welcome " + account.getMember().getName() + ". Please be informed that the phone " +
                    account.getMember().getPhone() + " is already in use with a verified account that belongs to " +
                    "some one else. Since your account was registered offline this inconsistency could not be anticipated " +
                    "for until you are connected to the internet. As a result you will need to change your phone number. " +
                    "We are sorry for any inconvenience caused. \n\nWould you like to change your phone number now?");

            messages.put(STATUS_ERR_EMAIL_ALREADY_IN_USE, "Welcome " + account.getMember().getName() + ". Please be informed that the emil " + account.getEmail() + " is already in use " +
                    "with a verified account that belongs to some one else. Since your account was registered offline this inconsistency " +
                    "could not be anticipated for until you are connected to the internet. As a result you will need to change your email. " +
                    "We are sorry for any inconvenience caused. \n\nWould you like to change your email now?");
        }

        return messages;
    }
}
