package com.mthoko.learners.domain.account;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.sms.Sms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class PhoneVerification extends UniqueEntity {

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String verificationCode;

    @Column(nullable = false)
    private Date expiryDate;

    @OneToOne
    private Sms verificationSms;

    public PhoneVerification() {
    }

    public PhoneVerification(String phoneNumber, String verificationCode, Date expiryDate, Sms verificationSms) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.expiryDate = expiryDate;
        this.verificationSms = verificationSms;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Sms getVerificationSms() {
        return verificationSms;
    }

    public void setVerificationSms(Sms verificationSms) {
        this.verificationSms = verificationSms;
    }

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(phoneNumber);
    }
}
