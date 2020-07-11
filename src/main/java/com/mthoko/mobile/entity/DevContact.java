package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Constraints;
import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.ForeignKey;
import com.mthoko.mobile.annotations.JoinColumn;
import com.mthoko.mobile.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mthokozisi_mhlanga on 28 Apr 2018.
 */

@Entity
public class DevContact extends UniqueEntity {

    @PrimaryKey
    private Long id;
    @Constraints(nullable = true)
    private Long verificationId;
    @ForeignKey(referencedEntity = Device.class)
    private Long devId;
    private String name;
    @JoinColumn(targetEntity = DevContactValue.class)
    private final List<DevContactValue> values;

    public DevContact() {
        this.values = new ArrayList<>();
    }

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, String> getPhones() {
        Map<Integer, String> phones = new LinkedHashMap<>();
        for (DevContactValue value : values) {
            if (value.getSource() < 100) {
                phones.put(value.getSource(), value.getValue());
            }
        }
        return phones;
    }

    public Map<Integer, String> getEmails() {
        Map<Integer, String> phones = new LinkedHashMap<>();
        for (DevContactValue value : values) {
            if (value.getSource() >= 100) {
                phones.put(value.getSource(), value.getValue());
            }
        }
        return phones;
    }

    public List<DevContactValue> getValues() {
        return values;
    }

    public void setValues(List<DevContactValue> values) {
        if (values == null) {
            return;
        }
        for (DevContactValue value : values) {
            value.setDevContactId(getId());
        }
        this.values.clear();
        this.values.addAll(values);
    }

    public void addContactValue(DevContactValue value) {
        if (value != null) {
            value.setDevContactId(getId());
            this.values.add(value);
        }
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
        return getUniqueIdentifierByList(getValues());
    }

    @Override
    public String toString() {
        return "DevContact{" +
                "id=" + id +
                ", verificationId=" + verificationId +
                ", devId=" + devId +
                ", name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
