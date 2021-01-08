package com.mthoko.mobile.entity;

import javax.persistence.Entity;

@Entity
public class SimContact extends UniqueEntity {
	
	private Long simCardId;

	private byte[] name;

	private String phone;

	public SimContact() {
	}

	public SimContact(String name, String phone) {
		setName(name);
		setPhone(phone);
	}

	public SimContact(Long simCardId, String name, String phone) {
		setSimCardId(simCardId);
		setName(name);
		setPhone(phone);
	}

	public String getName() {
		return byteArrayToString(name);
	}

	public void setName(String name) {
		this.name = stringToByteArray(name);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getSimCardId() {
		return simCardId;
	}

	public void setSimCardId(Long simCardId) {
		this.simCardId = simCardId;
	}

	@Override
	public String getUniqueIdentifier() {
		return phone;
	}

	@Override
	public String toString() {
		return "SimContact [id=" + getId() + ", simCardId=" + simCardId + ", name=" + getName() + ", phone=" + phone + "]";
	}

}
