package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Constraints;
import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.PrimaryKey;

/**
 * Created by Mthoko on 02 Oct 2018.
 */

@Entity
public class Credentials extends UniqueEntity {
    @PrimaryKey
    private Long id;
    @ForeignKey(referencedEntity = Member.class)
    private Long memberId;
    private String password;
    @Constraints(nullable = true)
    private Long verificationId;

    public Credentials() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return String.valueOf(password);
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", password='" + password + '\'' +
                ", verificationId=" + verificationId +
                '}';
    }
}