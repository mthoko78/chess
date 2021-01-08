package com.mthoko.mobile.entity;

import javax.persistence.Entity;

@Entity
public class SimCard extends UniqueEntity {

	private Long memberId;

	private String phone;

	private String simNo;

	public SimCard() {
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSimNo() {
		return simNo;
	}

	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}

	@Override
	public String getUniqueIdentifier() {
		return simNo + "|" + phone;
	}

	@Override
	public String toString() {
		return "SimCard [id=" + getId() + ", memberId=" + memberId + ", phone=" + phone + ", simNo=" + simNo + "]";
	}

}