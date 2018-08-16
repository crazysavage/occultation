package com.occultation.www;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-16 12:20
 */
public interface BeanContextFactory {

    <T> T getBean(String beanName, Class<T> clazz);
}
