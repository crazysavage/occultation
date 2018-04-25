package com.occultation.www.render.type;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:37
 */
public final class TypeHandlerRegistry {

    private static final Logger log = LoggerFactory.getLogger(TypeHandlerRegistry.class);

    private static final Map<Class,TypeHandler> HANDLERS = new HashMap<>();

    static {
        HANDLERS.put(int.class, new IntegerTypeHandler());
        HANDLERS.put(Integer.class, new IntegerTypeHandler());
        HANDLERS.put(Long.class, new LongTypeHandler());
        HANDLERS.put(long.class, new LongTypeHandler());
        HANDLERS.put(Float.class, new FloatTypeHandler());
        HANDLERS.put(float.class, new FloatTypeHandler());
        HANDLERS.put(Double.class, new DoubleTypeHandler());
        HANDLERS.put(double.class, new DoubleTypeHandler());
        HANDLERS.put(Boolean.class, new BooleanTypeHandler());
        HANDLERS.put(boolean.class, new BooleanTypeHandler());
        HANDLERS.put(Date.class, new DateTypeHandler());
        HANDLERS.put(String.class, new StringTypeHandler());
    }

    public static void registy(Class<?> clazz,TypeHandler typeHandler) {
        HANDLERS.put(clazz,typeHandler);
    }

    public static void unregister(Class<?> type) {
        HANDLERS.remove(type);
    }

    public static Object convert(Class<?> type, Object value) {
        TypeHandler th = HANDLERS.get(type);
        if (th != null) {
            return th.process(value);
        }
        log.error("{} typeHandler is undefined",type.getName());
        return value;
    }

    public static Object convertDate(Object value, String format) throws Exception {
        DateTypeHandler th = (DateTypeHandler) HANDLERS.get(Date.class);
        return th.getValue(value, format);
    }


}
