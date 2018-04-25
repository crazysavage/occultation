package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:41
 */
public class IntegerTypeHandler extends AbstractTypeHandler<Integer> {

    @Override
    protected Integer convert(Object value) {
        return Integer.parseInt(value.toString());
    }
}
