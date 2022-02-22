package com.mthoko.learners.domain.devcontact;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DevContactValue extends UniqueEntity {

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
