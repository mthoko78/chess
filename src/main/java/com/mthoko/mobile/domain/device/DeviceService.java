package com.mthoko.mobile.domain.device;

import com.mthoko.mobile.common.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface DeviceService extends BaseService<Device> {

	Optional<Device> findByImei(String imei);

	List<Device> findByMemberId(Long memberId);

}
