package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.PrimaryKey;

/**
 * Created by Mthoko on 05 May 2018.
 */

@Entity
public class SimContact extends UniqueEntity {
    @PrimaryKey
    private Long id;
    @ForeignKey(referencedEntity = SimCard.class)
    private Long simCardId;
    private Long verificationId;
    private String name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return phone;
    }

    @Override
    public String toString() {
        return "SimContact{" +
                "id=" + id +
                ", simCardId=" + simCardId +
                ", verificationId=" + verificationId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
