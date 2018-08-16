package com.occultation.www.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记在spiderBean上，表示该字段需要加入到爬虫队列中
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 17:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Href {
}
