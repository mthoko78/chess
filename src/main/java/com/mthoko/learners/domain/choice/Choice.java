package com.mthoko.learners.domain.choice;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class Choice extends UniqueEntity {

    private char letter;

    private String text;

    @Override
    public String getUniqueIdentifier() {
        return letter + "|" + text;
    }

    @Override
    public String toString() {
        return String.format("%s) %s", Character.toLowerCase(getLetter()), getText());
    }
}
