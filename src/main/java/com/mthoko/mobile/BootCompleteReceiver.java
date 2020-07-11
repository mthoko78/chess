package com.mthoko.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.mthoko.mobile.service.SmsService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.util.Date;

import static android.content.IntentFilter.SYSTEM_HIGH_PRIORITY;
import static com.mthoko.mobile.util.EntityMapper.getTimeStampFromDate;

/**
 * Created by mthokozisi_mhlanga on 28 Apr 2018.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    public BootCompleteReceiver() {
        Log.e("CREATED", getClass().getName());
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            handleBroadCast(context, intent);
        } catch (Exception e) {
            try {
                ServiceFactory.getSmsService(context).showNotification(e.getMessage());
            }
            catch (Exception e1) {
                e.printStackTrace();
            }
        }
    }

    private void registerCallListener(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        intentFilter.setPriority(SYSTEM_HIGH_PRIORITY);
        PhoneCallReceiver callReceiver = new CallReceiver();
        context.registerReceiver(callReceiver, intentFilter);
    }

    private void registerSmsListener(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(SYSTEM_HIGH_PRIORITY - 1);
        SmsBroadcastReceiver smsBroadcastReceiver = new SmsBroadcastReceiver();
        context.registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    private void handleBroadCast(Context context, Intent intent) {
        final SmsService smsService = ServiceFactory.getSmsService(context);
        String timeStamp = getTimeStampFromDate(new Date());
        smsService.setProperty("lastBoot", timeStamp);
        registerCallListener(context);
        registerSmsListener(context);
        smsService.showNotification("Device booted @ " + timeStamp);
    }
}