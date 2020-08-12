package com.mthoko.mobile.service.internal;

import java.util.Map;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.resource.DeviceResourceRemote;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class DeviceServiceImpl extends BaseServiceImpl implements DeviceService {

	private final DeviceResourceRemote deviceResourceRemote;

	public DeviceServiceImpl() {
		deviceResourceRemote = new DeviceResourceRemote(new ConnectionWrapper(null));
	}

	@Override
	public BaseResourceRemote<Device> getResource() {
		return deviceResourceRemote;
	}

	public void saveExternally(long externalMemberId, Device device) {
		deviceResourceRemote.save(externalMemberId, device);
	}

	public Device findRemoteDeviceByImei(String imei) {
		return deviceResourceRemote.findByImei(imei);
	}

	public Map<String, Long> retrieveVerification(Device device) {
		return deviceResourceRemote.retrieveVerificationByImei(device.getImei());
	}

}