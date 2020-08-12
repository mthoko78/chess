package com.mthoko.mobile.service.proxy;

import java.util.List;

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.resource.BaseResourceRemote;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.internal.BaseServiceImpl;
import com.mthoko.mobile.service.internal.SimContactServiceImpl;

public class SimContactServiceProxy extends BaseServiceImpl implements SimContactService {

	private final SimContactServiceImpl service;

	public SimContactServiceProxy() {
		service = new SimContactServiceImpl();
	}

	@Override
	public BaseResourceRemote<SimContact> getResource() {
		return service.getResource();
	}

	@Override
	public Integer countBySimNo(String simNo) {
		boolean openConnection = openConnection();
		Integer count = service.countBySimNo(simNo);
		closeConnectionIf(openConnection);
		return count;
	}

	@Override
	public List<SimContact> findBySimCardId(Long simCardId) {
		boolean openConnection = openConnection();
		List<SimContact> contacts = service.findBySimCardId(simCardId);
		closeConnectionIf(openConnection);
		return contacts;
	}

	@Override
	public List<SimContact> findBySimNo(String simNo) {
		boolean openConnection = openConnection();
		List<SimContact> contacts = service.findBySimNo(simNo);
		closeConnectionIf(openConnection);
		return contacts;
	}

	@Override
	public List<SimContact> findBySimNoExcludingIds(List<Long> ids, String simNo) {
		boolean openConnection = openConnection();
		List<SimContact> contacts = service.findBySimNoExcludingIds(ids, simNo);
		closeConnectionIf(openConnection);
		return contacts;
	}

}