package com.mthoko.mobile.entity;

import javax.persistence.Entity;

@Entity
public class Credentials extends UniqueEntity {

	private Long memberId;

	private String password;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUniqueIdentifier() {
		return String.valueOf(password);
	}

	@Override
	public String toString() {
		return "Credentials [id=" + getId() + ", memberId=" + memberId + ", password=" + password + "]";
	}

}