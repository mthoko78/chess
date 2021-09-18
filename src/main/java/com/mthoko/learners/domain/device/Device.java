package com.mthoko.learners.domain.device;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Device extends UniqueEntity {

	private Long memberId;

	private String imei;

	private String name;

	private Date dateRegistered;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getUniqueIdentifier() {
		return imei;
	}

	@Override
	public String toString() {
		return "Device [id=" + getId() + ", memberId=" + memberId + ", imei=" + imei + ", name=" + name + ", dateRegistered="
				+ dateRegistered + "]";
	}

}
