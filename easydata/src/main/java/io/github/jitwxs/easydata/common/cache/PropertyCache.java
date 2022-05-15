package io.github.jitwxs.easydata.common.cache;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.FieldNotFoundException;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
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

        private final BiConsumer<PropertyDescriptor, Map<String, PropertyDescriptor>> put = (e, map) -> map.put(processFieldName(e.getName()), e);

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
                fieldMap.putIfAbsent(processFieldName(field.getName()), field);
            }
        }

        private void addDescriptor(PropertyDescriptor descriptor) {
            put.accept(descriptor, all);

            try {
                fieldMap.put(processFieldName(descriptor.getName()), Whitebox.getField(this.target, descriptor.getName()));
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

        public PropertyDescriptor getDescriptor(final String name) {
            return this.all.get(name);
        }
    }

    /**
     * 受限于 java 自省的特性，对于 aB... 类型的字段，实际会处理成 AB...，需要做兼容处理
     * <p>
     * 其中 a 表示任意小写字符，B 表示任意大写字符。例如 "sId" will read "SId" in default
     *
     * @param field 字段名
     * @return 经过处理后的字段名
     */
    public static String processFieldName(final String field) {
        if (StringUtils.isNotBlank(field) && field.length() >= 2) {
            final char[] chars = field.toCharArray();
            if (Character.isLowerCase(chars[0]) && Character.isUpperCase(chars[1])) {
                chars[0] = Character.toUpperCase(chars[0]);
                return String.valueOf(chars);
            } else {
                return field;
            }
        } else {
            return field;
        }
    }
}
