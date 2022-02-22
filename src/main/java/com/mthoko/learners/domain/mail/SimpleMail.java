package com.mthoko.learners.domain.mail;

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
public class SimpleMail extends UniqueEntity {

    private String sender;

    private String recipient;

    private String subject;

    @Column(length = 4096)
    private String body;

    private Date dateSent;

    private Date dateDelivered;

    @Override
    public String getUniqueIdentifier() {
        return sender + "|" + recipient + "|" + dateSent;
    }

}
