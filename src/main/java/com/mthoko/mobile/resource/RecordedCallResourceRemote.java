package com.mthoko.mobile.resource;

import com.mthoko.mobile.entity.RecordedCall;
import com.mthoko.mobile.util.ConnectionWrapper;

public class RecordedCallResourceRemote extends BaseResourceRemote<RecordedCall> {

    public RecordedCallResourceRemote(Class<RecordedCall> entityType, ConnectionWrapper connectionWrapper) {
        super(entityType, connectionWrapper);
    }

}