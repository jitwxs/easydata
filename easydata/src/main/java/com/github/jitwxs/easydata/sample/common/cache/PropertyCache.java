package com.github.jitwxs.easydata.sample.common.cache;

import com.github.jitwxs.easydata.sample.common.exception.EasyDataException;
import com.github.jitwxs.easydata.sample.common.util.ObjectUtils;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import lombok.Getter;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.FieldNotFoundException;

import java.beans.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.beans.Introspector.getBeanInfo;

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
        private final Class<?> target;

        private final Map<String, PropertyDescriptor> all = new HashMap<>();

        private final Map<String, PropertyDescriptor> readable = new HashMap<>();

        private final Map<String, PropertyDescriptor> writeAble = new HashMap<>();

        private final Map<String, PropertyDescriptor> readAndWriteAble = new HashMap<>();

        private final Map<String, Field> fieldMap = new HashMap<>();

        private final BiConsumer<PropertyDescriptor, Map<String, PropertyDescriptor>> put = (e, map) -> map.put(e.getName(), e);

        public ClassProperty(Class<?> clazz) {
            this.target = clazz;

            try {
                final BeanInfo beanInfo = this.target.isInterface() ? getBeanInfo(this.target) : getBeanInfo(this.target, Object.class);
                final Map<String, PropertyDescriptor> descriptorMap = Arrays.stream(beanInfo.getPropertyDescriptors())
                        .collect(Collectors.toMap(FeatureDescriptor::getName, Function.identity()));

                if (Message.class.isAssignableFrom(clazz)) {
                    final Object object = ObjectUtils.createProtoBuilder(this.target);
                    if (object == null) {
                        throw new EasyDataException("ObjectUtils createProtoBuilder failed for " + this.target);
                    }

                    final List<Descriptors.FieldDescriptor> descriptorList = ((MessageOrBuilder) object).getDescriptorForType().getFields();
                    descriptorList.stream().map(e -> descriptorMap.get(e.getName())).filter(Objects::nonNull).forEach(this::addDescriptor);
                } else {
                    for (PropertyDescriptor descriptor : descriptorMap.values()) {
                        this.addDescriptor(descriptor);
                    }
                }
            } catch (Exception e) {
                throw new EasyDataException(String.format("Cache Class %s Property Failed", clazz), e);
            }

            for (Field field : clazz.getDeclaredFields()) {
                fieldMap.putIfAbsent(field.getName(), field);
            }
        }

        private void addDescriptor(PropertyDescriptor descriptor) {
            put.accept(descriptor, all);

            try {
                fieldMap.put(descriptor.getName(), Whitebox.getField(this.target, descriptor.getName()));
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
    }
}
