package com.mthoko.mobile.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.repo.DeviceRepo;
import com.mthoko.mobile.service.DeviceService;

@Service
public class DeviceServiceImpl extends BaseServiceImpl<Device> implements DeviceService {

	@Autowired
	private DeviceRepo deviceRepo;

	@Override
	public JpaRepository<Device, Long> getRepo() {
		return deviceRepo;
	}

	public void saveExternally(long externalMemberId, Device device) {
		device.setMemberId(externalMemberId);
		deviceRepo.save(device);
	}

	public Device findByImei(String imei) {
		return deviceRepo.findByImei(imei);
	}

	public Map<String, Long> retrieveVerification(Device device) {
		device = deviceRepo.findByImei(device.getImei());
		Map<String, Long> verification = new HashMap<>();
		if (device != null) {
			UniqueEntity.putVerification(device, verification);
		}
		return verification;
	}

	@Override
	public List<Device> findByMemberId(Long memberId) {
		return deviceRepo.findByMemberId(memberId);
	}

}