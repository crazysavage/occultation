package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:51
 */
public class FloatTypeHandler extends AbstractTypeHandler<Float> {
    @Override
    protected Float convert(Object value) {
        return Float.parseFloat(value.toString());
    }
}
