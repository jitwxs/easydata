package com.github.jitwxs.addax.common.cache;

import com.github.jitwxs.addax.common.exception.AddaxException;
import lombok.Getter;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

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

    @Getter
    public static class ClassProperty {
        private final Map<String, PropertyDescriptor> all = new HashMap<>();

        private final Map<String, PropertyDescriptor> readable = new HashMap<>();

        private final Map<String, PropertyDescriptor> writeAble = new HashMap<>();

        private final Map<String, PropertyDescriptor> readAndWriteAble = new HashMap<>();

        private final BiConsumer<PropertyDescriptor, Map<String, PropertyDescriptor>> put = (e, map) -> map.put(e.getName(), e);

        public ClassProperty(Class<?> clazz) {
            try {
                for (PropertyDescriptor descriptor : Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors()) {
                    put.accept(descriptor, all);
                    final boolean isReadable = descriptor.getReadMethod() != null, isWriteable = descriptor.getWriteMethod() != null;

                    if (isReadable) {
                        put.accept(descriptor, readable);
                    }

                    if (isWriteable) {
                        put.accept(descriptor, writeAble);
                    }

                    if (isReadable && isWriteable) {
                        put.accept(descriptor, readAndWriteAble);
                    }
                }
            } catch (IntrospectionException e) {
                throw new AddaxException(String.format("Cache Class %s Property Failed", clazz), e);
            }
        }
    }
}
