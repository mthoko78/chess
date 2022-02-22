package com.mthoko.learners.domain.category;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class Category extends UniqueEntity {

    private String name;

    private Integer number;

    private int totalQuestions;

    @Override
    public int compareTo(UniqueEntity that) {
        if (that.getClass() != getClass()) {
            return -1;
        }
        return this.name.compareTo(((Category) that).getName());
    }

    @Override
    public boolean equals(Object that) {
        return this.compareTo((Category) that) == 0;
    }

    @Override
    public String getUniqueIdentifier() {
        return String.valueOf(name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", id=" + getId() +
                ", number=" + number +
                ", totalQuestions=" + totalQuestions +
                '}';
    }
}