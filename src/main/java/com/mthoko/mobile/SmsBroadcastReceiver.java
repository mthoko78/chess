package com.mthoko.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.service.SmsService;

import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        new Thread() {
            @Override
            public void run() {
                try {
                    handleSmsBroadcast(context, intent);
                } catch (Exception e) {
                    try {
                        ServiceFactory.getSmsService(context).showNotification(e.getMessage());
                    }
                    catch (Exception e1) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void handleSmsBroadcast(Context context, Intent intent) {
        SmsService smsService = ServiceFactory.getSmsService(context);
        SimCardService simCardService = ServiceFactory.getSimCardService(context);
        if (intent != null && intent.getExtras() != null) {
            saveIncomingSms(intent, smsService, simCardService);
        }
    }

    public List<Sms> saveIncomingSms(Intent intent, SmsService smsService, SimCardService simCardService) {
        Bundle extras = intent.getExtras();
        List<Sms> smses = smsService.getSmsListFromBundle(extras);
        SimCard simCard = simCardService.findBySimNo(simCardService.getCurrentSimNo());
        for (Sms sms : smses) {
            sms.setRecipient(simCard.getPhone());
        }
        smsService.saveAll(smses);
        if (smsService.isConnectionAvailable()) {
            smsService.saveAllToRemote(smses);
        }
        return smses;
    }
}
