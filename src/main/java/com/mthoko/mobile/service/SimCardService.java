package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.SimCard;

public interface SimCardService extends BaseService<SimCard> {

	SimCard findBySimNo(String simNo);

	List<SimCard> findByMemberId(Long id);
}
