package com.mthoko.mobile.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Constraints {
    boolean autoIncrement() default false;

    boolean nullable() default false;

    String defaultValue() default "";
}
