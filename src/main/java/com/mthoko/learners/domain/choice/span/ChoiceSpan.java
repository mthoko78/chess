package com.mthoko.learners.domain.choice.span;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class ChoiceSpan extends UniqueEntity {

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
