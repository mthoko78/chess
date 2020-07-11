package com.mthoko.mobile.annotations;

import com.mthoko.mobile.entity.UniqueEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface ForeignKey {
    Class<? extends UniqueEntity> referencedEntity();
}