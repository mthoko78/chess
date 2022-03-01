package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DevContactValue extends BaseEntity {

    private Integer source;

    private String value;

    private Long devContactId;

    @Override
    public String getUniqueIdentifier() {
        return (source + "|" + value).replaceAll("[ \\+]", "");
    }

    @Override
    public String toString() {
        return "DevContactValue [id=" + getId() + ", source=" + source + ", value=" + value + ", contact="
                + devContactId + "]";
    }

}
