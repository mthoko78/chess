package com.mthoko.learners.domain.sms;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class MessageResponse extends UniqueEntity {

    @OneToMany
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(getId());
    }
}
