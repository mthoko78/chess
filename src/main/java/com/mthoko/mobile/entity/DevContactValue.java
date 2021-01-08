package com.mthoko.mobile.entity;

import javax.persistence.Entity;

@Entity
public class DevContactValue extends UniqueEntity {

	private Integer source;

	private String value;

	private Long devContactId;

	public DevContactValue() {
	}

	public DevContactValue(Integer source, String value) {
		this.source = source;
		this.value = value;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getDevContactId() {
		return devContactId;
	}

	public void setDevContactId(Long contact) {
		this.devContactId = contact;
	}

	@Override
	public String getUniqueIdentifier() {
		return source + "|" + value;
	}

	@Override
	public String toString() {
		return "DevContactValue [id=" + getId() + ", source=" + source + ", value=" + value + ", contact="
				+ devContactId + "]";
	}

}
