package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class SimCard extends BaseEntity {

    private Long memberId;

    private String phone;

    private String networkProvider;

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(phone);
    }

    @Override
    public String toString() {
        return "SimCard [id=" + getId() + ", memberId=" + memberId + ", phone=" + phone + "]";
    }

}