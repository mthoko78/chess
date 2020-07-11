package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.util.ConnectionWrapper;

public class RecordedCallResourceRemote extends BaseResourceRemote<RecordedCall> {

    public RecordedCallResourceRemote(Class<RecordedCall> entityType, Context context, ConnectionWrapper connectionWrapper) {
        super(entityType, context, connectionWrapper);
    }

}