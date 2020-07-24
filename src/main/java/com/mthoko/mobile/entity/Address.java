package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Constraints;
import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.PrimaryKey;

/**
 * Created by Mthoko on 02 Oct 2018.
 */

@Entity
public class Address extends UniqueEntity {
    @PrimaryKey
    private Long id;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String street;
    @Constraints(nullable = true)
    private Long verificationId;

    public Address() {
    }

    public boolean isValid() {
        return getId() != null;
    }

    public boolean isVerified() {
        return getVerificationId() != null;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getVerificationId() {
        return verificationId;
    }

    @Override
    public void setVerificationId(Long verificationId) {
        this.verificationId = verificationId;
    }

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
        return "Address{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", street='" + street + '\'' +
                ", verificationId=" + verificationId +
                '}';
    }
}