package com.mthoko.mobile.service;

import com.mthoko.mobile.entity.RecordedCall;

public interface RecordedCallService extends BaseService<RecordedCall> {

    String getDurationToString(int duration);

    RecordedCall retrieveRecentCall();
}
