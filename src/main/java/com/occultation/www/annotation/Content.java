package com.occultation.www.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记spiderBean的上下文
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 10:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Content {
    String url();

    String fetch() default "";

    String pipeline() default "";
}
