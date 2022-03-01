package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class QuestionImageMatch extends BaseEntity {

    private int imageNo;

    @OneToOne
    private QuestionImage questionImage;

    @Override
    public String getUniqueIdentifier() {
        return questionImage + "|" + imageNo;
    }

    @Override
    public String toString() {
        return "QuestionImageMatch [imageNo=" + imageNo + ", questionImage=" + questionImage + ", id=" + getId() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (getClass().equals(o.getClass())) {
            QuestionImageMatch that = (QuestionImageMatch) o;
            if (questionImage == null) {
                return that.questionImage == null;
            }
            return imageNo == that.imageNo && questionImage.equals(that.getQuestionImage());
        }
        return false;
    }

}
