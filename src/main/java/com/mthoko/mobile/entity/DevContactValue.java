package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.PrimaryKey;

@Entity
public class DevContactValue extends UniqueEntity {
    @PrimaryKey
    private Long id;
    @ForeignKey(referencedEntity = DevContact.class)
    private Long devContactId;
    private Long verificationId;
    private Integer source;
    private String value;

    public DevContactValue() {
    }

    public DevContactValue(Long devContactId, Integer source, String value) {
        this.devContactId = devContactId;
        this.source = source;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDevContactId() {
        return devContactId;
    }

    public void setDevContactId(Long devContactId) {
        this.devContactId = devContactId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return source + "|" + value;
    }

    @Override
    public String toString() {
        return "DevContactValue{" +
                "id=" + id +
                ", devContactId=" + devContactId +
                ", verificationId=" + verificationId +
                ", source=" + source +
                ", value='" + value + '\'' +
                '}';
    }
}
