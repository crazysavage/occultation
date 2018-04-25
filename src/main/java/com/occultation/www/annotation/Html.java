package com.occultation.www.annotation;

import com.occultation.www.enums.HtmlFieldTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liss
 * @created 2017年6月28日 下午1:53:54
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Html {
    String select();
    HtmlFieldTypeEnum type() default HtmlFieldTypeEnum.HTML;
    boolean outer() default false;
    String attr() default "";
}
