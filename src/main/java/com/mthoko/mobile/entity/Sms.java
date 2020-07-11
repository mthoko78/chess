package com.mthoko.mobile.entity;

import com.mthoko.mobile.annotations.Entity;
import com.mthoko.mobile.annotations.PrimaryKey;

import java.util.Date;

/**
 * Created by Mthoko on 24 Sep 2018.
 */

@Entity
public class Sms extends UniqueEntity {
    @PrimaryKey
    private Long id;
    private Long verificationId;
    private String sender;
    private String recipient;
    private String body;
    private Date date;
    private Date dateSent;
    private String location;
    private boolean sent;
    private boolean isRead;
    private boolean delivered;
    private boolean deliveryReportRequested;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getVerificationId() {
        return verificationId;
    }

    @Override
    public void setVerificationId(Long verificationId) {
        this.verificationId = verificationId;
    }

    @Override
    public String getUniqueIdentifier() {
        return sender + "|" + recipient + "|" + dateSent.getTime();
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id=" + id +
                ", verificationId=" + verificationId +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                ", dateSent='" + dateSent + '\'' +
                ", location='" + location + '\'' +
                ", sent=" + sent +
                ", isRead=" + isRead +
                ", delivered=" + delivered +
                ", deliveryReportRequested=" + deliveryReportRequested +
                '}';
    }

    public String getFormattedString() {
        StringBuilder body = new StringBuilder();
        body.append(String.format("From: %s\n", this.getSender()));
        body.append(String.format("To: %s\n", this.getRecipient()));
        body.append(String.format("Date received: %s\n", this.getDate()));
        body.append(String.format("Date sent: %s\n", this.getDateSent()));
        body.append(this.getBody() + "\n\n");
        return body.toString();
    }
}
