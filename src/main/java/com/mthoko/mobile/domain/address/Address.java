package com.mthoko.mobile.domain.address;

import com.mthoko.mobile.common.UniqueEntity;

import javax.persistence.Entity;

@Entity
public class Address extends UniqueEntity {

	private String country;

	private String state;

	private String city;

	private String postalCode;

	private String street;

	@Override
	public String getUniqueIdentifier() {
		return String.valueOf(state);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String toString() {
		return "Address [id=" + getId() + ", country=" + country + ", state=" + state + ", city=" + city
				+ ", postalCode=" + postalCode + ", street=" + street + "]";
	}

}