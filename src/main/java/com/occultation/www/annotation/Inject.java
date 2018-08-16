package com.occultation.www.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被标记的bean 会执行依赖注入，执行set（T t）方法
 *
 * @author yejy
 * @since 2018-08-16 11:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Inject {

}
