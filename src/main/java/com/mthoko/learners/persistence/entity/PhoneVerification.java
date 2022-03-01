package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhoneVerification extends BaseEntity {

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String verificationCode;

    @Column(nullable = false)
    private Date expiryDate;

    @OneToOne
    private Sms verificationSms;

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(phoneNumber);
    }
}
