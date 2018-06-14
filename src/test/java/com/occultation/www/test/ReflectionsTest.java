package com.occultation.www.test;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Fetch;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 13:49
 */
public class ReflectionsTest {
    public static void main(String[] args) {
        Reflections reflections = new Reflections(
            ConfigurationBuilder.build("com.occultation.www",new SubTypesScanner(),new TypeAnnotationsScanner()));

        Set<Class<?>> clazzs = reflections.getTypesAnnotatedWith(Content.class);

        for (Class<?> clazz : clazzs) {
            Fetch f = clazz.getAnnotation(Fetch.class);
            if (f != null) {
                System.out.println(f.value());
            }
            System.out.println(clazz.getCanonicalName());
        }

    }
}
