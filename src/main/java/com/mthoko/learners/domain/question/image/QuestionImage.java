package com.mthoko.learners.domain.question.image;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class QuestionImage extends UniqueEntity {

    private String path;

    private int width;

    private int height;

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(path) + "|" + width + "|" + height;
    }

    @Override
    public String toString() {
        return "QuestionImage{" +
                "path='" + path + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
