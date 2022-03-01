package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService extends BaseService<Device> {

	Optional<Device> findByImei(String imei);

	List<Device> findByMemberId(Long memberId);

}
