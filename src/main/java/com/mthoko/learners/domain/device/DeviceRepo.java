package com.mthoko.learners.domain.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {

    List<Device> findByMemberId(long memberId);

    Optional<Device> findByImei(String imei);
}