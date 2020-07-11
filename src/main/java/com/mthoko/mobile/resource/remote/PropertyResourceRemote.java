package com.mthoko.mobile.resource.remote;

import android.content.Context;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.util.ConnectionWrapper;

public class PropertyResourceRemote extends BaseResourceRemote<Property> {

	public PropertyResourceRemote(Context context, ConnectionWrapper connectionWrapper) {
		super(Property.class, context, connectionWrapper);
	}

	public String getProperty(String key) {
		Property property = findByKey(key);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	public Property findByKey(String key) {
		String whereClause = String.format("%s.key = '%s'", getEntityName(), key);
		Property property = findOneWhere(whereClause);
		return property;
	}

}
