package com.yndongyong.autogenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ad15 on 2017/9/25.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Arg {

    String value() default "";
}
