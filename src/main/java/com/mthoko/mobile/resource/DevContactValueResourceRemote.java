package com.mthoko.mobile.resource;

import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.util.ConnectionWrapper;

public class DevContactValueResourceRemote extends BaseResourceRemote<DevContactValue> {

	public DevContactValueResourceRemote(ConnectionWrapper connectionWrapper) {
		super(DevContactValue.class, connectionWrapper);
	}

}