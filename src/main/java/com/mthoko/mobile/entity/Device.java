package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Constraints;
import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.PrimaryKey;

import java.util.Date;

/**
 * Created by Mthoko on 12 Nov 2018.
 */

@Entity
public class Device extends UniqueEntity {
    @PrimaryKey
    private Long id;
    @ForeignKey(referencedEntity = Member.class)
    private Long memberId;
    private Long verificationId;
    private String imei;
    private String name;
    @Constraints(defaultValue = "CURRENT_DATE")
    private Date dateRegistered;

    public Device() {
    }

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
        return imei;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", verificationId=" + verificationId +
                ", imei='" + imei + '\'' +
                ", name='" + name + '\'' +
                ", dateRegistered='" + dateRegistered + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
