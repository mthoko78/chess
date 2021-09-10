package com.mthoko.mobile.domain.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationStampRepo extends JpaRepository<LocationStamp, Long> {

	List<LocationStamp> findByImei(String imei);

	LocationStamp findByImeiOrderByTimeCapturedDesc(String imei);

	Integer countByImei(String imei);
}