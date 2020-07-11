package com.mthoko.mobile.service;

import android.os.Bundle;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.Sms;

import java.util.List;

public interface SmsService extends BaseService<Sms> {

    void setProperty(String key, String value);

    void showNotification(String message);

    void integrateMessagesExternally(SimCard simCard);

    List<Sms> integrateMessagesInternally(SimCard simCard);

    List<Sms> getActualInbox();

    List<Sms> getActualSimInbox();

    List<Sms> getActualSent();

    List<Sms> getSmsListFromBundle(Bundle extras);

    void saveAllToRemote(List<Sms> smses);
}
