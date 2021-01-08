package com.mthoko.mobile.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthoko.mobile.entity.LocationStamp;

@Repository
public interface LocationStampRepo extends JpaRepository<LocationStamp, Long> {

	List<LocationStamp> findByImei(String imei);

	LocationStamp findByImeiOrderByTimeCapturedDesc(String imei);

	Integer countByImei(String imei);
}