package com.occultation.www.util;

import com.occultation.www.expections.BeanDefinedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 11:27
 */
public class BeanUtils {

    private static final Map<String,Map<String,Method>> METHOD_CACHE = new ConcurrentHashMap<>(64);

    public static void injectField(Object bean, Field field, Object value) {
        Method method = getMethod(bean.getClass(),field);
        method.setAccessible(true);
        try {
            if (value != null) {
                method.invoke(bean, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object getValue(Object bean, String fieldName) {
        Field field;
        try {
            field = bean.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(fieldName + "no defined in "+ bean.getClass().getName(),e);
        }
        return getValue(bean,field);
    }

    public static Object getValue(Object bean, Field field) {
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static Method getMethod(Class<?> clazz, Field field) {
        String typeName = clazz.getTypeName();
        String fieldName = field.getName();
        Map<String,Method> methodMap = METHOD_CACHE.get(typeName);
        if (MapUtils.isNotEmpty(methodMap)) {
            if (methodMap.containsKey(fieldName)) {
                return methodMap.get(fieldName);
            }
        } else {
            methodMap = new HashMap<>();
            METHOD_CACHE.put(typeName, methodMap);
        }
        try {
            Method method = clazz.getMethod(getMethodName(fieldName), field.getType());
            methodMap.put(fieldName,method);
            return method;
        } catch (NoSuchMethodException e) {
            throw new BeanDefinedException("the " + fieldName + " field in [" + typeName +"] have no set function",e);
        }
    }

    private static String getMethodName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return "set" + String.valueOf(cs);
    }
}
