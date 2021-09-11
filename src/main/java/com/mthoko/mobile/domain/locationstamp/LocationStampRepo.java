package com.mthoko.mobile.domain.locationstamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationStampRepo extends JpaRepository<LocationStamp, Long> {

	List<LocationStamp> findByImei(String imei);

	Optional<LocationStamp> findByImeiOrderByTimeCapturedDesc(String imei);

	Integer countByImei(String imei);
}