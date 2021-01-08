package com.mthoko.mobile.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Account extends UniqueEntity {

	@OneToOne
	private Member member;

	@OneToOne
	private Credentials credentials;

	@OneToMany
	private List<SimCard> simCards;

	@OneToMany
	private List<Device> devices;

	public Account() {
		this(new Member(), new Credentials(), new ArrayList<SimCard>(), new ArrayList<Device>());
	}

	public Account(Member member, Credentials credentials, List<SimCard> simCards, List<Device> devices) {
		this.credentials = credentials;
		this.simCards = simCards;
		this.devices = devices;
		this.member = member;
	}

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public void setSimCards(List<SimCard> simCards) {
		this.simCards = simCards;
	}

	public List<SimCard> getSimCards() {
		return simCards;
	}

	public String getPassword() {
		return getCredentials() == null ? null : getCredentials().getPassword();
	}

	public void setPassword(String password) {
		if (credentials != null) {
			getCredentials().setPassword(password);
		}
	}

	public String getEmail() {
		return getMember() == null ? null : getMember().getEmail();
	}

	public void setEmail(String email) {
		if (member != null) {
			member.setEmail(email);
		}
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public SimCard getSimCardBySimNo(String simNo) {
		for (SimCard sim : getSimCards())
			if (sim.getSimNo().equals(simNo))
				return sim;
		return null;
	}

	public SimCard getPrimarySimCard() {
		if (simCards == null || simCards.isEmpty()) {
			return null;
		}
		int indexOfMin = 0;
		for (int i = 0; i < simCards.size(); i++) {
			Long id = simCards.get(indexOfMin).getId();
			Long id2 = simCards.get(i).getId();
			if (id != null && id2 != null && id > id2) {
				indexOfMin = i;
			}
		}
		return simCards.get(indexOfMin);
	}

	public Device getDeviceByImei(String imei) {
		for (Device device : devices) {
			if (device.getImei().equals(imei)) {
				return device;
			}
		}
		return null;
	}

	public Device getPrimaryDevice() {
		if (devices.isEmpty()) {
			return null;
		}
		int indexOfMin = 0;
		for (int i = 0; i < devices.size(); i++) {
			Long id = devices.get(indexOfMin).getId();
			Long id2 = devices.get(i).getId();
			if (id != null && id2 != null && id > id2) {
				indexOfMin = i;
			}
		}
		return devices.get(indexOfMin);
	}

	@Override
	public String getUniqueIdentifier() {
		if (member != null) {
			return member.getUniqueIdentifier();
		}
		return getClass().getName() + ":" + getId();
	}

	public void setMemberId(Long memberId) {
		setId(memberId);
		if (member != null) {
			member.setId(memberId);
		}
		if (credentials != null) {
			credentials.setMemberId(memberId);
		}
		for (Device device : devices) {
			device.setMemberId(memberId);
		}
		for (SimCard simCard : simCards) {
			simCard.setMemberId(memberId);
		}
	}

	@Override
	public String toString() {
		return "Account [id=" + getId() + ", member=" + member + ", credentials=" + credentials + ", simCards="
				+ simCards + ", devices=" + devices + "]";
	}

}