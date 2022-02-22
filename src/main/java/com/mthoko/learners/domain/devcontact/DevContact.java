package com.mthoko.learners.domain.devcontact;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DevContact extends UniqueEntity {

    private Long devId;

    @Column(length = 1024)
    private byte[] name;

    @OneToMany
    private List<DevContactValue> values;

    public String getName() {
        return byteArrayToString(name);
    }

    public void setName(String name) {
        this.name = stringToByteArray(name);
    }

    @Override
    public String getUniqueIdentifier() {
        return getUniqueIdentifierByList(getValues());
    }

    @Override
    public String toString() {
        return "DevContact [id=" + getId() + ", devId=" + devId + ", name=" + getName() + ", values=" + values + "]";
    }

}
