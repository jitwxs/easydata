package com.github.jitwxs.addax.common.cache;

import com.github.jitwxs.addax.common.exception.AddaxException;
import lombok.Getter;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.FieldNotFoundException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
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

    public static Field tryGetField(final Class<?> clazz, final String fieldName) {
        return tryGet(clazz).getFieldMap().get(fieldName);
    }

    @Getter
    public static class ClassProperty {
        private final Map<String, PropertyDescriptor> all = new HashMap<>();

        private final Map<String, PropertyDescriptor> readable = new HashMap<>();

        private final Map<String, PropertyDescriptor> writeAble = new HashMap<>();

        private final Map<String, PropertyDescriptor> readAndWriteAble = new HashMap<>();

        private final Map<String, Field> fieldMap = new HashMap<>();

        private final BiConsumer<PropertyDescriptor, Map<String, PropertyDescriptor>> put = (e, map) -> map.put(e.getName(), e);

        public ClassProperty(Class<?> clazz) {
            try {
                final BeanInfo beanInfo = clazz.isInterface() ? Introspector.getBeanInfo(clazz) : Introspector.getBeanInfo(clazz, Object.class);

                for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                    put.accept(descriptor, all);

                    try {
                        fieldMap.put(descriptor.getName(), Whitebox.getField(clazz, descriptor.getName()));
                    } catch (FieldNotFoundException ignored) {
                    }

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
