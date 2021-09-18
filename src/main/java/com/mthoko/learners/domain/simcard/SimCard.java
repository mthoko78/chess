package com.mthoko.learners.domain.simcard;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Entity;

@Entity
public class SimCard extends UniqueEntity {

    private Long memberId;

    private String phone;

    private String networkProvider;

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

    public String getNetworkProvider() {
        return networkProvider;
    }

    public void setNetworkProvider(String networkProvider) {
        this.networkProvider = networkProvider;
    }

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(phone);
    }

    @Override
    public String toString() {
        return "SimCard [id=" + getId() + ", memberId=" + memberId + ", phone=" + phone + "]";
    }

}