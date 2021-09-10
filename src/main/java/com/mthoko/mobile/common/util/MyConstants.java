package com.mthoko.mobile.common.util;

/**
 * Created by Mthoko on 20 Oct 2018.
 */

public interface MyConstants { // 'public static final' modifiers are prepended by default

    /*
    * NUMERIC CONSTANTS
    * */

    int CODE_WRITE_EXTERNAL_STORAGE = 1;
    int CODE_RECORD_AUDIO = 2;
    int CODE_READ_PHONE_STATE = 3;
    int CODE_READ_CONTACTS = 4;
    int CODE_WRITE_CONTACTS = 5;
    int CODE_RECEIVE_BOOT_COMPLETED= 6;
    int CODE_ACCESS_FINE_LOCATION = 7;
    int CODE_ACCESS_COARSE_LOCATION = 8;
    int CODE_INTERNET = 9;
    int CODE_READ_SMS = 10;
    int CODE_ALL_PERMISSIONS = 11;

    int STATUS_ALREADY_VERIFIED = 12;           // very safe
    int STATUS_VERIFIED_FIRST_TIME = 13;
    int STATUS_OVERWRITTEN_BY_EX_ACCOUNT = 14;                // unsafe
    int STATUS_ERR_EMAIL_PHONE_ALREADY_IN_USE_DIFF_ACCOUNTS = 15;
    int STATUS_ERR_EMAIL_ALREADY_IN_USE = 16;
    int STATUS_ERR_EMAIL_PHONE_ALREADY_IN_USE = 17;
    int STATUS_ERR_PHONE_ALREADY_IN_USE = 18;

    int STATUS_NATIVE_SUCCESS_VERIFIED = 19;
    int STATUS_FOREIGN_SUCCESS_VERIFIED = 20;
    int STATUS_NATIVE_SUCCESS_UNVERIFIED = 21;
    int STATUS_FOREIGN_SUCCESS_UNVERIFIED = 22;
    int STATUS_NATIVE_INCORRECT_PASSWORD = 23;
    int STATUS_FOREIGN_ACCOUNT_DOES_NOT_EXIST = 24;
    int STATUS_FOREIGN_INCORRECT_PASSWORD_VERIFIED = 25;
    int STATUS_FOREIGN_NOT_FOUND_NO_INTERNET = 26;
    int STATUS_FOREIGN_INCORRECT_PASSWORD_UNVERIFIED = 27;
    int STATUS_FOREIGN_FAIL_ACCOUNT_UPDATED = 28;

    int STATUS_REGISTERED_EXT_FIRST_TIME = 29;
    int STATUS_REGISTERED_INT_FIRST_TIME = 30;

    int STATUS_OFFLINE_ACCOUNT_NOT_VERIFIED = 31;
    int STATUS_OFFLINE_NO_INTERNAL_ACCOUNT = 32;
    int STATUS_ACCOUNT_DOES_NOT_EXIST = 33;
    int STATUS_JSON_EXCEPTION = 34;
    int STATUS_UNKNOWN_ERROR = 35;
    int RESULT_STATUS = 36;

    int STATUS_SUCCESS = 37;
    int STATUS_INCORRECT_PASSWORD = 38;
    //public static final int CODE_WRITE_SMS = 11;


    int STATUS_SUCCESS_VERIFIED = 39; // registered successfully and verified
    int STATUS_FAIL_VERIFIED_DUP_PHONE = 40; // cannot register: phone has been registered and verified
    int STATUS_FAIL_VERIFIED_DUP_EMAIL = 41; // cannot register: email has been registered and verified
    int STATUS_FAIL_VERIFIED_DUP_PHONE_EMAIL = 42;  // cannot register: phone and email has been registered and verified

    int STATUS_SUCCESS_UNVERIFIED = 43; // registered successfully and verified
    int STATUS_FAIL_UNVERIFIED_DUP_PHONE = 44; // cannot register: phone has been registered but not verified
    int STATUS_FAIL_UNVERIFIED_DUP_EMAIL = 45; // cannot register: email has been registered but not verified
    int STATUS_FAIL_UNVERIFIED_DUP_PHONE_EMAIL = 46; // cannot register: phone and email has been registered but not verified
    int STATUS_FAIL_SIM_ALREADY_REGISTERED = 47;
    int STATUS_FAIL_DUP_ENTRIES = 48;
    int STATUS_FAIL_INTERNAL_ACCOUNT_NEEDS_ATTENTION = 49;
    int STATUS_FOREIGN_SUCCESS_VERIFIED_OVERWRITTEN = 50;
    int STATUS_FOREIGN_INCORRECT_PASSWORD_VERIFIED_OVERWRITTEN = 51;
    int STATUS_NO_INTERNET_CONNECTION = 52;


    int ACTION_LOAD_EXTERNAL_SIM_CONTACTS = 53;
    int ACTION_LOAD_SIM_CONTACT_VERIFICATION_IDS = 54;
    int ACTION_BACKUP_SIM_CONTACTS = 55;
    int STATUS_FAIL_DEVICE_ALREADY_ALLOCATED = 56;


    /*
     * TEXTUAL CONSTANTS
     * */


    

}
