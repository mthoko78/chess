package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import com.mthoko.learners.persistence.entity.Device;
import com.mthoko.learners.persistence.repository.DeviceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	public Optional<Device> findByImei(String imei) {
		return deviceRepo.findByImei(imei);
	}

	public Map<String, Long> retrieveVerification(Device device) {
		Optional<Device> optionalDevice = deviceRepo.findByImei(device.getImei());
		Map<String, Long> verification = new HashMap<>();
		if (optionalDevice.isPresent()) {
			BaseEntity.putVerification(optionalDevice.get(), verification);
		}
		return verification;
	}

	@Override
	public List<Device> findByMemberId(Long memberId) {
		return deviceRepo.findByMemberId(memberId);
	}

}