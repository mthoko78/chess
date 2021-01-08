package com.mthoko.mobile.service.internal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.repo.SimCardRepo;
import com.mthoko.mobile.service.SimCardService;

@Service
public class SimCardServiceImpl extends BaseServiceImpl<SimCard> implements SimCardService {

	@Autowired
	private SimCardRepo simCardRepo;

	@Override
	public JpaRepository<SimCard, Long> getRepo() {
		return simCardRepo;
	}

	@Override
	public SimCard findBySimNo(String simNo) {
		return simCardRepo.findBySimNo(simNo);
	}

	@Override
	public List<SimCard> findByMemberId(Long id) {
		return simCardRepo.findByMemberId(id);
	}

}
