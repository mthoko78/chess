package com.mthoko.learners.domain.sms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Sms extends UniqueEntity {

    private String sender;

    private String recipient;

    private String recipientImei;

    @Column(length = 10240)
    private byte[] body;

    private Date date;

    private Date dateSent;

    private String location;

    private boolean sent;

    private boolean isRead;

    private boolean delivered;

    private boolean deliveryReportRequested;

    private boolean mailed;

    private String messageId;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return byteArrayToString(body);
    }

    public void setBody(String body) {
        this.body = stringToByteArray(body);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isDeliveryReportRequested() {
        return deliveryReportRequested;
    }

    public void setDeliveryReportRequested(boolean deliveryReportRequested) {
        this.deliveryReportRequested = deliveryReportRequested;
    }

    public boolean isMailed() {
        return mailed;
    }

    public void setMailed(boolean mailed) {
        this.mailed = mailed;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRecipientImei() {
        return recipientImei;
    }

    public void setRecipientImei(String recipientImei) {
        this.recipientImei = recipientImei;
    }

    @Override
    public String getUniqueIdentifier() {
        return sender + "|" + recipient + "|" + (dateSent == null ? null : dateSent.getTime());
    }

    @JsonIgnore
    public String getFormattedString() {
        StringBuilder body = new StringBuilder();
        body.append(String.format("From: %s\n", this.getSender()));
        body.append(String.format("To: %s\n", this.getRecipient()));
        body.append(String.format("Date received: %s\n", this.getDate()));
        body.append(String.format("Date sent: %s\n\n", this.getDateSent()));
        body.append(this.getBody());
        return body.toString();
    }

    @Override
    public String toString() {
        return "Sms{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", body=" + byteArrayToString(body) +
                ", date=" + date +
                ", dateSent=" + dateSent +
                ", location='" + location + '\'' +
                ", sent=" + sent +
                ", isRead=" + isRead +
                ", delivered=" + delivered +
                ", deliveryReportRequested=" + deliveryReportRequested +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
