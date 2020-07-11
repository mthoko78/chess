package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.RecordedCall;

import java.util.Date;

public class RecordedCallResourceApi extends BaseResourceApi<RecordedCall> {

    public RecordedCallResourceApi(Context context) {
        super(context, RecordedCall.class);
    }

    public Long save(String category, String number, String contactName, Date timestamp, int duration, long fileInfoId) {
        return save(new RecordedCall(null, category, number, contactName, timestamp, duration, fileInfoId));
    }
}