package com.mthoko.mobile.resource;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.util.ConnectionWrapper;

public class PropertyResourceRemote extends BaseResourceRemote<Property> {

	public PropertyResourceRemote(ConnectionWrapper connectionWrapper) {
		super(Property.class, connectionWrapper);
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
