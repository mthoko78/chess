package com.mthoko.learners.domain.sms;

import com.mthoko.learners.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsDeliveryReport extends BaseEntity {

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

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(timestamp);
    }
}
