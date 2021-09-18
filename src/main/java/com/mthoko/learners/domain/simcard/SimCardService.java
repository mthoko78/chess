package com.mthoko.learners.domain.simcard;

import com.mthoko.learners.common.service.BaseService;

import java.util.List;

public interface SimCardService extends BaseService<SimCard> {

	List<SimCard> findByMemberId(Long id);
}
