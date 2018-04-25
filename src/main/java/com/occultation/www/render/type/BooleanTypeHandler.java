package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:53
 */
public class BooleanTypeHandler extends AbstractTypeHandler<Boolean> {
    @Override
    protected Boolean convert(Object value) {
        return Boolean.parseBoolean(value.toString());
    }
}
