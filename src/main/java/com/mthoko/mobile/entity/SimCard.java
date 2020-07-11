package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.PrimaryKey;


/**
 * Created by Mthoko on 06 Oct 2018.
 */

@Entity
public class SimCard extends UniqueEntity {
    @PrimaryKey
    private Long id;
    @ForeignKey(referencedEntity = Member.class)
    private Long memberId;
    private Long verificationId;
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



//=============================OVERRIDDEN CONTEXT==========================================
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
        return simNo + "|" + phone;
    }

    @Override
    public String toString() {
        return "SimCard{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", verificationId=" + verificationId +
                ", phone='" + phone + '\'' +
                ", simNo='" + simNo + '\'' +
                '}';
    }
}