package com.lyn.cleansing.utils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.reflections.Reflections;
import com.lyn.cleansing.handler.processor.BaseCleansingProcessor;

/**
 * @author linkaidi
 * @date 2023/7/28
 */
public class ClassUtils {

    public static <T> List<Class<? extends T>> getSubClasses(Class<T> baseClass) {
        List<Class<? extends T>> subClasses = new ArrayList<>();
        String packageName = baseClass.getPackage().getName();
        Reflections reflections = new Reflections(packageName);

        for (Class<?> clazz : reflections.getSubTypesOf(baseClass)) {
            if (isSubclassOf(clazz, baseClass)) {
                subClasses.add((Class<? extends T>) clazz);
            }
        }

        return subClasses;
    }

    private static boolean isSubclassOf(Class<?> clazz, Class<?> baseClass) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType.equals(baseClass)) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                for (Type typeArgument : typeArguments) {
                    if (typeArgument instanceof Class) {
                        Class<?> typeArgumentClass = (Class<?>) typeArgument;
                        if (!baseClass.isAssignableFrom(typeArgumentClass)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }
}
