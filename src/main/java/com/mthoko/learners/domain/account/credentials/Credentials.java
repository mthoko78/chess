package com.mthoko.learners.domain.account.credentials;

import com.mthoko.learners.common.entity.BaseEntity;
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
public class Credentials extends BaseEntity {

    private Long memberId;

    private String password;

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(password);
    }

    @Override
    public String toString() {
        return "Credentials [id=" + getId() + ", memberId=" + memberId + ", password=" + password + "]";
    }

}