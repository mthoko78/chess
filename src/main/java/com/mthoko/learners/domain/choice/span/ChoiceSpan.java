package com.mthoko.learners.domain.choice.span;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Entity;

@Entity
public final class ChoiceSpan extends UniqueEntity {

    private String romFigure;

    private String text;

    public ChoiceSpan() {
    }

    public ChoiceSpan(String romFigure, String text) {
        this.romFigure = romFigure;
        this.text = text;
    }

    public String getRomFigure() {
        return romFigure;
    }

    public void setRomFigure(String romFigure) {
        this.romFigure = romFigure;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getUniqueIdentifier() {
        return romFigure + "|" + text;
    }

    @Override
    public String toString() {
        return String.format("(%s) %s", getRomFigure(), getText());
    }
}
