package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.DeviceServiceImpl;

public class DeviceServiceProxy extends BaseServiceImpl implements DeviceService {

	private final DeviceServiceImpl service;

	public DeviceServiceProxy() {
		service = new DeviceServiceImpl();
	}

	@Override
	public Device findRemoteDeviceByImei(String imei) {
		boolean connection = service.openConnection();
		Device device = service.findRemoteDeviceByImei(imei);
		service.closeConnectionIf(connection);
		return device;
	}

	@Override
	public BaseResourceRemote<Device> getResource() {
		return service.getResource();
	}
}
