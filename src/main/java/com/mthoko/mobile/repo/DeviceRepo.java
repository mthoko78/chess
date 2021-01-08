package com.mthoko.mobile.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.Device;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {

	public List<Device> findByMemberId(long memberId);

	public Device findByImei(String imei);
}