package com.mthoko.mobile.resource.remote;

import com.mthoko.mobile.entity.Address;
import com.mthoko.mobile.util.ConnectionWrapper;

public class AddressResource extends BaseResourceRemote<Address> {

	public AddressResource(ConnectionWrapper connectionWrapper) {
		super(Address.class, connectionWrapper);
	}

}
