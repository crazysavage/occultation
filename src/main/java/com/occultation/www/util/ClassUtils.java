package com.occultation.www.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static boolean isBaseType(Class<?> a) {
        return a.isPrimitive()
            || a.equals(String.class)
            || a.equals(Integer.class)
            || a.equals(Long.class)
            || a.equals(Double.class)
            || a.equals(Byte.class)
            || a.equals(Boolean.class);
    }



    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

}
