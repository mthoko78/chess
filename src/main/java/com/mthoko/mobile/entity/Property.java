package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.PrimaryKey;

@Entity
public class Property extends UniqueEntity {

    @PrimaryKey
    private Long id;

    private Long verificationId;

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return key;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", verificationId=" + verificationId +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
