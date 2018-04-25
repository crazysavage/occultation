package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:55
 */
public class StringTypeHandler extends AbstractTypeHandler<String> {

    @Override
    protected String convert(Object value) {
        return value.toString();
    }
}
