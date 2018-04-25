package com.occultation.www.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 17:20
 */
public class ClassUtils {

    public static Class getGenericClass(Type type) {
        if (type instanceof ParameterizedType) {
            Type next = ((ParameterizedType) type).getActualTypeArguments()[0];
            return getGenericClass(next);
        }
        return (Class) type;
    }

    public static boolean isSubType(Class<?> a, Class<?> b) {
        return b.isAssignableFrom(a);

    }

}
