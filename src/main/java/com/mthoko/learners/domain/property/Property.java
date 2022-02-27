package com.mthoko.learners.domain.property;

import com.mthoko.learners.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Property extends BaseEntity {

    private String propertyKey;

    @Column(length = 4096)
    private String propertyValue;

    @Override
    public String getUniqueIdentifier() {
        return propertyKey;
    }

    @Override
    public String toString() {
        return "Property [id=" + getId() + ", key=" + propertyKey + ", value=" + propertyValue + "]";
    }

}
