package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:49
 */
public class LongTypeHandler extends AbstractTypeHandler<Long> {
    @Override
    protected Long convert(Object value) {
        return Long.parseLong(value.toString());
    }
}
