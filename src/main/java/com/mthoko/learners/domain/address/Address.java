package com.mthoko.learners.domain.address;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address extends UniqueEntity {

    private String country;

    private String state;

    private String city;

    private String postalCode;

    private String street;

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(state);
    }

    @Override
    public String toString() {
        return "Address [id=" + getId() + ", country=" + country + ", state=" + state + ", city=" + city
                + ", postalCode=" + postalCode + ", street=" + street + "]";
    }

}