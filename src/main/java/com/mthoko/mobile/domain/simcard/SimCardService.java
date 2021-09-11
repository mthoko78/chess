package com.mthoko.mobile.domain.simcard;

import com.mthoko.mobile.common.service.BaseService;

import java.util.List;

public interface SimCardService extends BaseService<SimCard> {

	List<SimCard> findByMemberId(Long id);
}
