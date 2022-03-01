package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message extends BaseEntity {

    private String apiMessageId;

    @Column(name = "recipient")
    private String to;

    private boolean accepted;

    private Integer errorCode;

    private String errorDescription;

    private String error;

    @Override
    public String getUniqueIdentifier() {
        if (apiMessageId != null) {
            return apiMessageId;
        }
        if (errorCode != null) {
            return error + "|date:" + getDateCreated();
        }
        return String.valueOf(getDateCreated());
    }
}
