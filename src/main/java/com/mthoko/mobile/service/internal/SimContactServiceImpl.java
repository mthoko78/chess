package com.mthoko.mobile.service.internal;

import java.util.List;

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.resource.SimContactResourceRemote;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class SimContactServiceImpl extends BaseServiceImpl implements SimContactService {

	private final SimContactResourceRemote resource;

	public SimContactServiceImpl() {
		resource = new SimContactResourceRemote(new ConnectionWrapper(null));
	}

	@Override
	public BaseResourceRemote<SimContact> getResource() {
		return resource;
	}

	@Override
	public Integer countBySimNo(String simNo) {
		return resource.countBySimNo(simNo);
	}

	@Override
	public List<SimContact> findBySimCardId(Long simCardId) {
		return resource.findBySimCardId(simCardId);
	}

	@Override
	public List<SimContact> findBySimNo(String simNo) {
		return resource.findBySimNo(simNo);
	}

	@Override
	public List<SimContact> findBySimNoExcludingIds(List<Long> ids, String simNo) {
		return resource.findBySimNoExcludingIds(ids, simNo);
	}
}
