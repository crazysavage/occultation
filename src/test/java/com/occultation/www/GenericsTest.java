package com.occultation.www;

import com.occultation.www.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 13:17
 */
public class GenericsTest {
    private static int step = 0;

    static class Example<T extends String>{
        List<String> x;

        public Example() {
            x = new ArrayList<>();
        }

    }

    public static void main(String[] args) {

        Field f = Example.class.getDeclaredFields()[0];
        Type type = f.getGenericType();
        System.out.println(f.getType());

        System.out.println(ClassUtils.isSubType(f.getType(),List.class));

        consoleType(type);
    }

    public static void consoleType(Type type) {
        if (type instanceof TypeVariable) {
            System.out.println("********TypeVariable step-> "+(++step)+"********");
            Type next = ((TypeVariable) type).getBounds()[0];
            System.out.println(next);
            consoleType(next);
            return;
        }
        if (type instanceof ParameterizedType) {
            System.out.println("********ParameterizedType step-> "+(++step)+"********");
            Type next = ((ParameterizedType) type).getActualTypeArguments()[0];
            System.out.println("rawType = " + ((ParameterizedType) type).getRawType());
            System.out.println(next);
            consoleType(next);
            return;
        }
        if (type instanceof GenericArrayType) {
            System.out.println("********GenericArrayType step-> "+(++step)+"********");
            Type next = ((GenericArrayType) type).getGenericComponentType();
            System.out.println(next);
            consoleType(next);
            return;
        }


        System.out.println("********finally step-> "+(++step)+"********");
        System.out.println(type);



    }

}
