package com.mthoko.mobile.domain.question.image;

import com.mthoko.mobile.common.entity.UniqueEntity;

import javax.persistence.Entity;

/**
 * @author Mthoko
 */
@Entity
public final class QuestionImage extends UniqueEntity {

    private String path;

    private int width;

    private int height;

    public QuestionImage() {
    }

    public QuestionImage(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
