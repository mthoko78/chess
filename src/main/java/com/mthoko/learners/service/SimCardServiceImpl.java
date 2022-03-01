package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.SimCard;
import com.mthoko.learners.persistence.repository.SimCardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimCardServiceImpl extends BaseServiceImpl<SimCard> implements SimCardService {

	@Autowired
	private SimCardRepo simCardRepo;

	@Override
	public JpaRepository<SimCard, Long> getRepo() {
		return simCardRepo;
	}

	@Override
	public List<SimCard> findByMemberId(Long id) {
		return simCardRepo.findByMemberId(id);
	}

}
