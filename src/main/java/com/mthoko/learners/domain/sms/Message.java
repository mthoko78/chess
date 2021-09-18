package com.mthoko.learners.domain.sms;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Message extends UniqueEntity {

    private String apiMessageId;

    @Column(name = "recipient")
    private String to;

    private boolean accepted;

    private Integer errorCode;

    private String errorDescription;

    private String error;

    public String getApiMessageId() {
        return apiMessageId;
    }

    public void setApiMessageId(String apiMessageId) {
        this.apiMessageId = apiMessageId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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
