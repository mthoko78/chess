package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.SimCard;

import java.util.List;

public interface SimCardService extends BaseService<SimCard> {

	List<SimCard> findByMemberId(Long id);
}
