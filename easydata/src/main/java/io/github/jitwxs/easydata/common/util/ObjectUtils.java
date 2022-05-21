package io.github.jitwxs.easydata.common.util;

import com.google.protobuf.Message;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.common.function.ThrowableBiFunction;
import io.github.jitwxs.easydata.common.function.ThrowableFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 18:38
 */
@Slf4j
public class ObjectUtils {
    private static final ObjectFactory delegate = new DefaultObjectFactory();

    /**
     * 根据 class 创建对象，智能调用构造方法
     *
     * @param target create object's class
     * @param <T>    create object's generic
     * @return instance
     */
    public static <T> T create(Class<T> target) {
        try {
            return delegate.create(target);
        } catch (ReflectionException e) {
            log.error("ObjectUtils create error, target: {}", target, e);
            return null;
        }
    }

    /**
     * 根据 class 创建对象，指定构造方法
     *
     * @param target              create object's class
     * @param <T>                 create object's generic
     * @param constructorArgTypes 构造方法参数类型
     * @param constructorArgs     构造方法参数
     * @return instance
     */
    public static <T> T create(Class<T> target, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return delegate.create(target, constructorArgTypes, constructorArgs);
    }

    /**
     * 根据 class 创建 protobuf builder 对象
     *
     * @param target create object's class
     * @return protobuf builder object instance
     */
    public static Object createProtoBuilder(Class<?> target) {
        if (!Message.class.isAssignableFrom(target)) {
            throw new EasyDataException("ObjectUtils not allow createProto for " + target);
        }

        try {
            final Method builder = Whitebox.getMethod(target, "newBuilder");
            return builder.invoke(null);
        } catch (Exception e) {
            log.error("ObjectUtils createProto error, target: {}", target, e);
            return null;
        }
    }

    /**
     * 将 protobuf builder 对象，构造成 proto 对象
     *
     * @param protoBuilder protobuf builder object instance
     * @return protobuf object instance
     */
    public static Object buildProtoBuilder(Object protoBuilder) {
        final Class<?> target = protoBuilder.getClass();

        try {
            final Method build = Whitebox.getMethod(target, "build");

            return build.invoke(protoBuilder);
        } catch (Exception e) {
            log.error("ObjectUtils buildProtoBuilder error, target: {}", target, e);
            return null;
        }
    }

    /**
     * 构造 proto 对象，并填充属性
     *
     * @param target             proto 对象类型
     * @param fieldGeneratorFunc 属性生成方法
     * @param <T>                proto 对象类型
     * @return proto 对象
     * @throws IllegalAccessException    if this {@code Method} object is enforcing Java language access control
     *                                   and the underlying method is inaccessible.
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws Throwable                 内部流程处理异常
     */
    public static <T> T createJava(final Class<T> target,
                                   final ThrowableFunction<Field, Boolean> fieldIgnoreGeneratorFunc,
                                   final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc) throws Throwable {
        final Object result = ObjectUtils.create(target);

        for (Map.Entry<String, FieldProperty> entry : PropertyCache.tryGet(target).getWriteAble().entrySet()) {
            final String fieldName = entry.getKey();

            final FieldProperty property = entry.getValue();

            final Field field = property.getField();

            if (field == null || fieldIgnoreGeneratorFunc.apply(field)) {
                continue;
            }

            final Object fieldValue = fieldGeneratorFunc.apply(fieldName, field.getGenericType());
            if (fieldValue != null) {
                property.getWriteFunc().apply(result, fieldValue);
            }
        }

        return (T) result;
    }

    /**
     * 构造 proto 对象，并填充属性
     *
     * @param target             proto 对象类型
     * @param fieldGeneratorFunc 属性生成方法
     * @param <T>                proto 对象类型
     * @return proto 对象
     * @throws IllegalAccessException    if this {@code Method} object is enforcing Java language access control
     *                                   and the underlying method is inaccessible.
     * @throws InvocationTargetException if the underlying method throws an exception.
     */
    public static <T> T createProto(final Class<T> target,
                                    final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc) throws Throwable {
        final Object invoke = ObjectUtils.createProtoBuilder(target);
        if (invoke == null) {
            return null;
        }

        for (Map.Entry<String, FieldProperty> entry : PropertyCache.tryGet(target).getReadable().entrySet()) {
            final String fieldName = entry.getKey();

            final String writeMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method method = null;
            try {
                method = invoke.getClass().getDeclaredMethod(writeMethodName, entry.getValue().getTarget());
            } catch (NoSuchMethodException e) {
                log.error("ObjectUtils createProto error, class: {}, field: {}", target, fieldName);
            }

            if (method != null) {
                final Object fieldValue = fieldGeneratorFunc.apply(fieldName, entry.getValue().getType());
                if (fieldValue != null) {
                    method.invoke(invoke, fieldValue);
                }
            }
        }

        return (T) ObjectUtils.buildProtoBuilder(invoke);
    }
}
