package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.util.ConnectionWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceResourceRemote extends BaseResourceRemote<Device> {

	public DeviceResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
		super(Device.class, context, connectionWrapper);
	}

	public long save(Long memberId, Device device) {
		device.setMemberId(memberId);
		return save(device);
	}

	public List<Device> findByMemberId(long memberId) {
		return findWhereJoining(Member.class, getEntityClass(),
				String.format("%s.memberId = %s", getEntityName(), memberId));
	}

	public Device findByImei(String imei) {
		return findOneWhere(String.format("%s.imei = '%s'", getEntityName(), imei));
	}

	public Map<String, Long> retrieveVerificationByImei(String imei) {
		Device device = findByImei(imei);
		if (device != null) {
			return extractVerification(device);
		}
		return new HashMap<>();
	}
}