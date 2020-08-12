package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.SimContact;

public interface SimContactService extends BaseService {

	Integer countBySimNo(String simNo);

	List<SimContact> findBySimCardId(Long simCardId);

	List<SimContact> findBySimNo(String simNo);

	List<SimContact> findBySimNoExcludingIds(List<Long> ids, String simNo);
}
