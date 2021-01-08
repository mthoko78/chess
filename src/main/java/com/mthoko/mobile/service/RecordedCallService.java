package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.RecordedCall;

public interface RecordedCallService extends BaseService<RecordedCall> {

	List<RecordedCall> findByCallerOrSimNoOrImeiExcludingIds(List<Long> remoteIds, String caller, String simNo,
			String imei);

	List<RecordedCall> findByCallerOrSimNoOrImei(String caller, String simNo, String imei);
}
