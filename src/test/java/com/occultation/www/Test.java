package com.occultation.www;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-10-26 12:57
 */
public class Test {

    public static void main(String[] args) {

        Test test = new Test();

        Type t = test.getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type entityClass = ((ParameterizedType) t).getActualTypeArguments()[0];
            System.out.println(entityClass);
        } else {
            System.out.println("---------");
        }

    }
}
