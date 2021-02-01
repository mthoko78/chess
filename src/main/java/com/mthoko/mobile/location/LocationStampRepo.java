package com.mthoko.mobile.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationStampRepo extends JpaRepository<LocationStamp, Long> {

	List<LocationStamp> findByImei(String imei);

	LocationStamp findByImeiOrderByTimeCapturedDesc(String imei);

	Integer countByImei(String imei);
}