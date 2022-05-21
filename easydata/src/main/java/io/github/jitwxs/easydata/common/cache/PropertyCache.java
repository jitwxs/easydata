package io.github.jitwxs.easydata.common.cache;

import io.github.jitwxs.easydata.common.bean.ClassProperty;
import io.github.jitwxs.easydata.common.bean.FieldProperty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性缓存
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 20:07
 */
public class PropertyCache {
    /**
     * cache class's property descriptor info
     */
    private static final Map<Class<?>, ClassProperty> classPropertyMap = new ConcurrentHashMap<>();

    public static ClassProperty tryGet(final Class<?> clazz) {
        return classPropertyMap.computeIfAbsent(clazz, i -> new ClassProperty(clazz));
    }

    public static FieldProperty tryGet(final Class<?> clazz, final String fieldName) {
        return tryGet(clazz).getAll().get(fieldName);
    }
}
