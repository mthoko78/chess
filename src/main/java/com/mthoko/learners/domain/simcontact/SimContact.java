package com.mthoko.learners.domain.simcontact;

import com.mthoko.learners.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimContact extends BaseEntity {

    private Long simCardId;

    private byte[] name;

    private String phone;

    public String getName() {
        return byteArrayToString(name);
    }

    public void setName(String name) {
        this.name = stringToByteArray(name);
    }

    @Override
    public String getUniqueIdentifier() {
        return phone;
    }

    @Override
    public String toString() {
        return "SimContact [id=" + getId() + ", simCardId=" + simCardId + ", name=" + getName() + ", phone=" + phone + "]";
    }

}
