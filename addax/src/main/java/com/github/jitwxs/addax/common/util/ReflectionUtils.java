package com.github.jitwxs.addax.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 18:01
 */
public class ReflectionUtils {
    /**
     * 父类中获取子类泛型
     */
    public static Type[] getGenericSuperClass(final Class<?> clazz) {
        Type t = clazz.getGenericSuperclass();

        if (t instanceof  ParameterizedType) {
            return ((ParameterizedType) t).getActualTypeArguments();
        }

        if (t instanceof Class) {
            return getGenericSuperClass((Class<?>) t);
        }

        return null;
    }

    public static Type[] getGenericInterface0Class(final Class<?> clazz) {
        final Type t = clazz.getGenericInterfaces()[0];
        ParameterizedType p = (ParameterizedType) t ;
        return p.getActualTypeArguments();
    }

    public static void loadSuperAndInterfaceClass(final Class<?> clazz, final List<Class<?>> resultList) {
        for (Type interfaceType : clazz.getGenericInterfaces()) {
            final Class<?> actualClass = resolveActualClass(interfaceType);
            if (actualClass != null) {
                resultList.add(actualClass);
                loadSuperAndInterfaceClass(actualClass, resultList);
            }
        }

        final Type superType = clazz.getGenericSuperclass();
        if (superType != null) {
            final Class<?> actualClass = resolveActualClass(superType);
            if (actualClass != null) {
                resultList.add(actualClass);
                loadSuperAndInterfaceClass(actualClass, resultList);
            }
        }
    }

    private static Class resolveActualClass(final Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return resolveActualClass(((ParameterizedType) type).getRawType());
        }
        return null;
    }
}
