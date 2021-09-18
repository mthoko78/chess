package com.mthoko.learners.domain.sms;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Entity;

@Entity
public class SmsDeliveryReport extends UniqueEntity {

    private String integrationName;

    private String messageId;

    private String requestId;

    private String clientMessageId;

    private String recipient;

    private String sender;

    private String timestamp;

    private String status;

    private String statusDescription;

    private Integer statusCode;

    public SmsDeliveryReport() {
    }

    public SmsDeliveryReport(String integrationName, String messageId, String requestId, String clientMessageId, String recipient, String sender, String timestamp, String status, String statusDescription, Integer statusCode) {
        this.integrationName = integrationName;
        this.messageId = messageId;
        this.requestId = requestId;
        this.clientMessageId = clientMessageId;
        this.recipient = recipient;
        this.sender = sender;
        this.timestamp = timestamp;
        this.status = status;
        this.statusDescription = statusDescription;
        this.statusCode = statusCode;
    }

    public String getIntegrationName() {
        return integrationName;
    }

    public void setIntegrationName(String integrationName) {
        this.integrationName = integrationName;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClientMessageId() {
        return clientMessageId;
    }

    public void setClientMessageId(String clientMessageId) {
        this.clientMessageId = clientMessageId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(timestamp);
    }
}
