package com.mthoko.mobile.service.common;

import android.content.Context;

import com.mthoko.mobile.resource.internal.MyResources;
import com.mthoko.mobile.service.AccountService;
import com.mthoko.mobile.service.CredentialsService;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.LocationStampService;
import com.mthoko.mobile.service.MemberService;
import com.mthoko.mobile.service.RecordedCallService;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.proxy.AccountServiceProxy;
import com.mthoko.mobile.service.proxy.CredentialsServiceProxy;
import com.mthoko.mobile.service.proxy.DevContactServiceProxy;
import com.mthoko.mobile.service.proxy.DeviceServiceProxy;
import com.mthoko.mobile.service.proxy.FTPServiceProxy;
import com.mthoko.mobile.service.proxy.LocationStampServiceProxy;
import com.mthoko.mobile.service.proxy.MemberServiceProxy;
import com.mthoko.mobile.service.proxy.RecordedCallServiceProxy;
import com.mthoko.mobile.service.proxy.SimCardServiceProxy;
import com.mthoko.mobile.service.proxy.SimContactServiceProxy;
import com.mthoko.mobile.service.proxy.SmsServiceProxy;

public class ServiceFactory {

    public static AccountService getAccountService(Context context) {
        return new AccountServiceProxy(parseContext(context));
    }

    public static DevContactService getContactService(Context context) {
        return new DevContactServiceProxy(parseContext(context));
    }

    public static SimContactService getSimContactService(Context context) {
        return new SimContactServiceProxy(parseContext(context));
    }

    public static CredentialsService getCredentialsService(Context context) {
        return new CredentialsServiceProxy(parseContext(context));
    }

    public static DeviceService getDeviceService(Context context) {
        return new DeviceServiceProxy(parseContext(context));
    }

    public static MemberService getMemberService(Context context) {
        return new MemberServiceProxy(parseContext(context));
    }

    public static RecordedCallService getRecordedCallService(Context context) {
        return new RecordedCallServiceProxy(parseContext(context));
    }

    public static RecordingService getRecordingService(Context  context) {
        return new RecordingService();
    }

    public static SimCardService getSimCardService(Context context) {
        return new SimCardServiceProxy(parseContext(context));
    }

    private static Context parseContext(Context context) {
        if (context == null) {
            context = MyResources.getInstance().getContext();
        }
        return context;
    }

    public static LocationService getLocationService(Context context) {
        return new LocationService(parseContext(context));
    }

    public static SmsService getSmsService(Context context) {
        return new SmsServiceProxy(parseContext(context));
    }

    public static FTPService getFtpService(Context context) {
        return new FTPServiceProxy(parseContext(context));
    }

    public static LocationStampService getLocationStampService(Context context) {
        return new LocationStampServiceProxy(context);
    }
}
