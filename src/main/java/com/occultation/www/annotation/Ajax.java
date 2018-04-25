package com.occultation.www.annotation;

import com.occultation.www.enums.HttpMethodEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-05 16:05
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Ajax {
    String src();

    HttpMethodEnum method() default HttpMethodEnum.GET;

}
