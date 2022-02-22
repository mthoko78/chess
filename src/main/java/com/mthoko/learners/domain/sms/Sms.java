package com.mthoko.learners.domain.sms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sms extends UniqueEntity {

    private String sender;

    private String recipient;

    private String recipientImei;

    @Column(length = 4096)
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

    public String getBody() {
        return byteArrayToString(body);
    }

    public void setBody(String body) {
        this.body = stringToByteArray(body);
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
