package com.mthoko.mobile.location;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.common.BaseServiceImpl;

@Service
public class LocationStampServiceImpl extends BaseServiceImpl<LocationStamp> implements LocationStampService {

	@Autowired
	private LocationStampRepo locationStampRepo;

	public List<LocationStamp> findByImei(String imei) {
		return locationStampRepo.findByImei(imei);
	}

	@Override
	public JpaRepository<LocationStamp, Long> getRepo() {
		return locationStampRepo;
	}

	@Override
	public LocationStamp findMostRecentByImei(String imei) {
		return locationStampRepo.findByImeiOrderByTimeCapturedDesc(imei);
	}

	@Override
	public Integer countByImei(String imei) {
		return locationStampRepo.countByImei(imei);
	}
}