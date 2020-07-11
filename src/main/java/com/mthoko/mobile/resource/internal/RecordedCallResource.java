package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.util.DatabaseWrapper;

public class RecordedCallResource extends BaseResource<RecordedCall> {

    public RecordedCallResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, RecordedCall.class, databaseWrapper);
    }
}