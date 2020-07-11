package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.util.ConnectionWrapper;

public class DevContactValueResourceRemote extends BaseResourceRemote<DevContactValue> {

	public DevContactValueResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
		super(DevContactValue.class, context, connectionWrapper);
	}

}