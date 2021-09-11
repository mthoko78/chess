package com.mthoko.mobile.domain.account.member;

import com.mthoko.mobile.common.entity.UniqueEntity;

import javax.persistence.Entity;

@Entity
public class Member extends UniqueEntity {

	private String name;

	private String surname;

	private String phone;

	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getUniqueIdentifier() {
		return email + "|" + phone;
	}

	@Override
	public String toString() {
		return "Member [id=" + getId() + ", name=" + name + ", surname=" + surname + ", phone=" + phone + ", email="
				+ email + "]";
	}

}