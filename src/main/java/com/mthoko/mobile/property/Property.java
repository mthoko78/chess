package com.mthoko.mobile.property;

import javax.persistence.Entity;

import com.mthoko.mobile.common.UniqueEntity;

@Entity
public class Property extends UniqueEntity {

	private String propertyKey;

	private String propertyValue;

	public Property(String key, String value) {
		this.propertyKey = key;
		this.propertyValue = value;
	}

	public Property() {
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String key) {
		this.propertyKey = key;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String value) {
		this.propertyValue = value;
	}

	@Override
	public String getUniqueIdentifier() {
		return propertyKey;
	}

	@Override
	public String toString() {
		return "Property [id=" + getId() + ", key=" + propertyKey + ", value=" + propertyValue + "]";
	}

}
