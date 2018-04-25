package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:38
 */
public interface TypeHandler<T> {
    T process(Object value);
}
