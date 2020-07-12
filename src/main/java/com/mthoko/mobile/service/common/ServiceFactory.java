package com.mthoko.mobile.service.common;

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

    public static AccountService getAccountService() {
        return new AccountServiceProxy();
    }

    public static DevContactService getContactService() {
        return new DevContactServiceProxy();
    }

    public static SimContactService getSimContactService() {
        return new SimContactServiceProxy();
    }

    public static CredentialsService getCredentialsService() {
        return new CredentialsServiceProxy();
    }

    public static DeviceService getDeviceService() {
        return new DeviceServiceProxy();
    }

    public static MemberService getMemberService() {
        return new MemberServiceProxy();
    }

    public static RecordedCallService getRecordedCallService() {
        return new RecordedCallServiceProxy();
    }

    public static SimCardService getSimCardService() {
        return new SimCardServiceProxy();
    }

    public static SmsService getSmsService() {
        return new SmsServiceProxy();
    }

    public static FTPService getFtpService() {
        return new FTPServiceProxy();
    }

    public static LocationStampService getLocationStampService() {
        return new LocationStampServiceProxy();
    }

	public static MailService getMailService() {
		return new MailService();
	}
}
