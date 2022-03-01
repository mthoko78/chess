package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class ChoiceSpan extends BaseEntity {

    private String romFigure;

    private String text;

    @Override
    public String getUniqueIdentifier() {
        return romFigure + "|" + text;
    }

    @Override
    public String toString() {
        return String.format("(%s) %s", getRomFigure(), getText());
    }
}
