package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:50
 */
public class DoubleTypeHandler extends AbstractTypeHandler<Double> {
    @Override
    protected Double convert(Object value) {
        return Double.parseDouble(value.toString());
    }
}
