package io.github.jitwxs.easydata.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 18:01
 */
public class ReflectionUtils {
    /**
     * 子类获取继承父类中的泛型
     *
     * @param target 子类
     * @return 父类中的泛型信息
     */
    public static Type[] getGenericSuperClass(final Class<?> target) {
        Type t = target.getGenericSuperclass();

        if (t instanceof ParameterizedType) {
            return ((ParameterizedType) t).getActualTypeArguments();
        }

        if (t instanceof Class) {
            return getGenericSuperClass((Class<?>) t);
        }

        return null;
    }

    /**
     * 子类获取接口父类中的泛型
     *
     * @param target 子类
     * @return 父类接口中的泛型信息
     */
    public static Type[] getGenericInterface0Class(final Class<?> target) {
        final Type t = target.getGenericInterfaces()[0];
        ParameterizedType p = (ParameterizedType) t;
        return p.getActualTypeArguments();
    }

    /**
     * 获取某类的所有父类和接口，递归查询
     *
     * @param target     目标类
     * @param resultList 结果集
     */
    public static void loadSuperAndInterfaceClass(final Class<?> target, final List<Class<?>> resultList) {
        for (Type interfaceType : target.getGenericInterfaces()) {
            final Class<?> actualClass = resolveActualClass(interfaceType);
            if (actualClass != null) {
                resultList.add(actualClass);
                loadSuperAndInterfaceClass(actualClass, resultList);
            }
        }

        final Type superType = target.getGenericSuperclass();
        if (superType != null) {
            final Class<?> actualClass = resolveActualClass(superType);
            if (actualClass != null) {
                resultList.add(actualClass);
                loadSuperAndInterfaceClass(actualClass, resultList);
            }
        }
    }

    private static Class<?> resolveActualClass(final Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return resolveActualClass(((ParameterizedType) type).getRawType());
        }
        return null;
    }
}
