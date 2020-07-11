package com.mthoko.mobile.util;

import java.lang.reflect.Field;

public class EntityJoin {
    public final String firstEntity;
    public final String firstColumn;
    public final String secondEntity;
    public final String secondColumn;

    public EntityJoin(Class<?> firstEntity, String firstColumn, Class<?> secondEntity, String secondColumn) {
        this.firstEntity = firstEntity.getSimpleName();
        this.firstColumn = firstColumn;
        this.secondEntity = secondEntity.getSimpleName();
        this.secondColumn = secondColumn;
    }

    public EntityJoin(Class<?> firstEntity, Field firstColumn, Class<?> secondEntity, Field secondColumn) {
        this(firstEntity, firstColumn.getName(), secondEntity, secondColumn.getName());
    }


    public String getJoinClause() {
        return String.format(" JOIN %s ON %s.%s = %s.%s",
                secondEntity,
                firstEntity,
                firstColumn,
                secondEntity,
                secondColumn
        );
    }

}
