package com.mthoko.mobile.service.proxy;

import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.AddressService;
import com.mthoko.mobile.service.internal.AddressServiceImpl;
import com.mthoko.mobile.service.internal.BaseServiceImpl;

public class AddressServiceProxy extends BaseServiceImpl implements AddressService {
	
	private AddressServiceImpl service = new AddressServiceImpl();

	@Override
	public BaseResourceRemote<?> getResource() {
		return service.getResource();
	}

}
