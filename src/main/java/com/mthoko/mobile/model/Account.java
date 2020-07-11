package com.mthoko.mobile.model;

import com.mthoko.mobile.entity.Credentials;
import com.mthoko.mobile.entity.Device;
import com.mthoko.mobile.entity.Member;
import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.entity.UniqueEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mthoko on 02 Oct 2018.
 */

public class Account extends UniqueEntity {
    private Member member;
    private Credentials credentials;
    private final List<SimCard> simCards;
    private final List<Device> devices;

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
        if (getCredentials() != null) {
            getCredentials().setMemberId(member.getId());
        }
        for (SimCard simCard : getSimCards()) {
            simCard.setMemberId(member.getId());
        }
        for (Device device : getDevices()) {
            device.setMemberId(member.getId());
        }
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setSimCards(List<SimCard> simCards) {
        this.simCards.clear();
        if (simCards != null) {
            this.simCards.addAll(simCards);
        }
    }

    public List<SimCard> getSimCards() {
        return simCards;
    }

    public String getPassword() {
        return getCredentials().getPassword();
    }

    public void setPassword(String password) {
        getCredentials().setPassword(password);
    }

    public boolean isValid() {
        return this.member.isValid();
    }

    public boolean isVerified() {
        return member.isVerified();
    }

    public String getEmail() {
        return getMember().getEmail();
    }

    public void setEmail(String email) {
        getMember().setEmail(email);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices.clear();
        if (devices != null) {
            this.devices.addAll(devices);
        }
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
            if (simCards.get(indexOfMin).getId() > simCards.get(i).getId()) {
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
        return devices.get(0);
    }

    @Override
    public Long getId() {
        if (member != null) {
            return member.getId();
        }
        return null;
    }

    @Override
    public void setId(Long id) {
        if (member != null) {
            member.setId(id);
            if (credentials != null) {
                credentials.setMemberId(id);
            }
            for (Device device : devices) {
                device.setMemberId(id);
            }
            for (SimCard simCard : simCards) {
                simCard.setMemberId(id);
            }
        }
    }

    @Override
    public Long getVerificationId() {
        if (member != null) {
            return member.getVerificationId();
        }
        return null;
    }

    @Override
    public void setVerificationId(Long verificationId) {
        if (member != null) {
            member.setVerificationId(verificationId);
        }
    }

    @Override
    public String getUniqueIdentifier() {
        if (member != null) {
            return member.getUniqueIdentifier();
        }
        return getClass().getName();
    }

    @Override
    public String toString() {
        String toStr = String.format("{\"member\":%s,\"credentials\":%s,\"simCards\":%s}",
                getMember().toString(), getCredentials().toString(), simCards);
        return toStr;
    }
}