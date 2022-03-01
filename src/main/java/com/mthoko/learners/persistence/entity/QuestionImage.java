package com.mthoko.learners.persistence.entity;

import com.mthoko.learners.persistence.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class QuestionImage extends BaseEntity {

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
