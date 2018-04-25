package com.occultation.www.render.type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:42
 */
public abstract class AbstractTypeHandler<T> implements TypeHandler<T> {
    @Override
    public T process(Object value) {
        if (value != null) {
            try {
                return convert(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected abstract T convert(Object value) throws Exception;
}
