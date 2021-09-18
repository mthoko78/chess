package com.mthoko.learners.domain.account;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.account.credentials.Credentials;
import com.mthoko.learners.domain.account.member.Member;
import com.mthoko.learners.domain.device.Device;
import com.mthoko.learners.domain.simcard.SimCard;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

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

    private String primaryPhone;

    private String primaryImei;

    private boolean phoneVerified;

    public Account() {
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

    private SimCard getPrimarySimCard() {
        for (SimCard simCard : simCards) {
            if (simCard.getPhone().equals(primaryPhone)) {
                return simCard;
            }
        }
        return null;
    }

    private void setPrimarySimCard(SimCard primarySimCard) {
        for (SimCard simCard : simCards) {
            if (simCard.equals(primarySimCard)) {
                this.primaryPhone = simCard.getPhone();
                break;
            }
        }
    }

    private Device getPrimaryDevice() {
        for (Device device : devices) {
            if (device.getImei().equals(primaryImei)) {
                return device;
            }
        }
        return null;
    }

    private void setPrimaryDevice(Device primaryDevice) {
        for (Device device : devices) {
            if (device.equals(primaryDevice)) {
                this.primaryImei = device.getImei();
                break;
            }
        }
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getPrimaryImei() {
        return primaryImei;
    }

    public void setPrimaryImei(String primaryImei) {
        this.primaryImei = primaryImei;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
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
        if (devices != null) {
            for (Device device : devices) {
                device.setMemberId(memberId);
            }
        }
        if (simCards != null) {
            for (SimCard simCard : simCards) {
                simCard.setMemberId(memberId);
            }
        }
    }

    @Override
    public String toString() {
        return "Account [id=" + getId() + ", member=" + member + ", credentials=" + credentials + ", simCards="
                + simCards + ", devices=" + devices + "]";
    }

}