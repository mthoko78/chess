package com.mthoko.mobile.service.internal;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.resource.remote.AddressResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.util.ConnectionWrapper;

public class AddressServiceImpl extends BaseServiceImpl implements BaseService {

	private final BaseResourceRemote<Address> resource;

	public AddressServiceImpl() {
		resource = new AddressResource(new ConnectionWrapper(null));
	}
	
	@Override
	public BaseResourceRemote<Address> getResource() {
		return resource;
	}

}
