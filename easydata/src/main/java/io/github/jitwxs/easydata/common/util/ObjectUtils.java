package io.github.jitwxs.easydata.common.util;

import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.ClassGroupEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.common.function.ThrowableBiFunction;
import io.github.jitwxs.easydata.common.function.ThrowableFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.MethodNotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
     * @param target              create object's class
     * @param constructorSupplier 构造器方法
     * @param <T>                 create object's generic
     * @return instance
     */
    public static <T> T create(Class<T> target, Supplier<T> constructorSupplier) {
        try {
            // 基于默认无参构造方法
            return delegate.create(target);
        } catch (ReflectionException e) {
            // 使用提供的构造器方法
            if (constructorSupplier != null) {
                return constructorSupplier.get();
            }

            // 使用 builder 构造器兜底
            final Object builder = createBuilder(target, ClassGroupEnum.Group.NATIVE);
            if (builder != null) {
                return (T) buildBuilder(builder);
            }
        }

        throw new EasyDataException("failed create object, please try provide create function to resolve this exception");
    }

    /**
     * 根据 class 创建 protobuf builder 对象
     *
     * @param target create object's class
     * @param group  specify class type
     * @return protobuf builder object instance
     */
    public static Object createBuilder(Class<?> target, ClassGroupEnum.Group group) {
        try {
            final String methodName;
            switch (group) {
                case NATIVE:
                    methodName = "builder";
                    break;
                case PROTOBUF:
                    methodName = "newBuilder";
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            final Method builder = Whitebox.getMethod(target, methodName);
            return builder.invoke(null);
        } catch (MethodNotFoundException e) {
            return null;
        } catch (Exception e) {
            log.error("ObjectUtils createBuilder error, target: {}", target, e);
            return null;
        }
    }

    /**
     * 将 protobuf builder 对象，构造成 proto 对象
     *
     * @param builderBean builder object instance
     * @return protobuf object instance
     */
    public static Object buildBuilder(Object builderBean) {
        final Class<?> target = builderBean.getClass();

        try {
            final Method build = Whitebox.getMethod(target, "build");

            return build.invoke(builderBean);
        } catch (Exception e) {
            log.error("ObjectUtils buildBuilder error, target: {}", target, e);
            return null;
        }
    }

    /**
     * 构造 proto 对象，并填充属性
     *
     * @param target                   proto 对象类型
     * @param constructorSupplier      构造器方法
     * @param fieldIgnoreGeneratorFunc 属性是否忽略判断
     * @param newInstanceConsume       当对象构造完毕后，回调消费方法
     * @param fieldGeneratorFunc       属性生成方法
     * @param <T>                      proto 对象类型
     * @return proto 对象
     * @throws IllegalAccessException    if this {@code Method} object is enforcing Java language access control
     *                                   and the underlying method is inaccessible.
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws Throwable                 内部流程处理异常
     */
    public static <T> T createJava(final Class<T> target,
                                   final Supplier<T> constructorSupplier,
                                   final ThrowableFunction<Field, Boolean> fieldIgnoreGeneratorFunc,
                                   final Consumer<Object> newInstanceConsume,
                                   final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc) throws Throwable {
        final Object invoke = ObjectUtils.create(target, constructorSupplier);

        if (newInstanceConsume != null) {
            newInstanceConsume.accept(invoke);
        }

        for (Map.Entry<String, FieldProperty> entry : PropertyCache.tryGet(target).getWriteAble().entrySet()) {
            final String fieldName = entry.getKey();

            final FieldProperty property = entry.getValue();

            final Field field = property.getField();

            if (field == null || fieldIgnoreGeneratorFunc.apply(field)) {
                continue;
            }

            try {
                final Object fieldValue = fieldGeneratorFunc.apply(fieldName, field.getGenericType());
                if (fieldValue != null) {
                    property.getWriteFunc().apply(invoke, fieldValue);
                }
            } catch (Exception e) {
                log.warn("ObjectUtils#createJava setField failure, target: {}, field: {}", target, fieldName);
            }
        }

        return (T) invoke;
    }

    /**
     * 构造 proto message 对象，并填充属性
     *
     * @param target             proto message 对象类型
     * @param newInstanceConsume 当对象构造完毕后，回调消费方法
     * @param fieldGeneratorFunc 属性生成方法
     * @param <T>                proto message 对象类型
     * @return proto message 对象
     * @throws IllegalAccessException    if this {@code Method} object is enforcing Java language access control
     *                                   and the underlying method is inaccessible.
     * @throws InvocationTargetException if the underlying method throws an exception.
     */
    public static <T> T createProtoMessage(final Class<T> target,
                                           final Consumer<Object> newInstanceConsume,
                                           final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc) throws Throwable {
        final Object builder = ObjectUtils.createBuilder(target, ClassGroupEnum.Group.PROTOBUF);
        if (builder == null) {
            return null;
        }

        fillingProtoBuilderField(target, builder, fieldGeneratorFunc);

        final T invoke = (T) ObjectUtils.buildBuilder(builder);

        if (newInstanceConsume != null) {
            newInstanceConsume.accept(invoke);
        }

        return invoke;
    }

    /**
     * 构造 proto builder 对象，并填充属性
     *
     * @param target             proto builder 对象类型
     * @param newInstanceConsume 当对象构造完毕后，回调消费方法
     * @param fieldGeneratorFunc 属性生成方法
     * @param <T>                proto builder 对象类型
     * @return proto builder 对象
     * @throws IllegalAccessException    if this {@code Method} object is enforcing Java language access control
     *                                   and the underlying method is inaccessible.
     * @throws InvocationTargetException if the underlying method throws an exception.
     */
    public static <T> T createProtoBuilder(final Class<T> target,
                                           final Consumer<Object> newInstanceConsume,
                                           final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc) throws Throwable {
        final T invoke = ObjectUtils.create(target, null);

        if (invoke == null) {
            return null;
        }

        final Class<?> protoMessageClass = ReflectionUtils.getProtoMessageClassByBuilder(target);

        fillingProtoBuilderField(protoMessageClass, invoke, fieldGeneratorFunc);

        if (newInstanceConsume != null) {
            newInstanceConsume.accept(invoke);
        }

        return invoke;
    }

    private static void fillingProtoBuilderField(final Class<?> protoMessageClass,
                                                 final Object protoBuilder,
                                                 final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc) throws Throwable {
        final Class<?> protoBuilderClass = protoBuilder.getClass();

        for (Map.Entry<String, FieldProperty> entry : PropertyCache.tryGet(protoMessageClass).getReadable().entrySet()) {
            final String fieldName = entry.getKey();
            final FieldProperty fieldProperty = entry.getValue();

            // support protobuf repeat grammar
            final String writeMethodName = (fieldProperty.getTarget() == Iterable.class ? "addAll" : "set") +
                    fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method method = null;
            try {
                method = protoBuilderClass.getDeclaredMethod(writeMethodName, fieldProperty.getTarget());
            } catch (NoSuchMethodException e) {
                log.error("ObjectUtils fillingProtoBuilderField error, protoMessageClass: {}, field: {}", protoMessageClass, fieldName);
            }

            if (method != null) {
                final Object fieldValue = fieldGeneratorFunc.apply(fieldName, fieldProperty.getType());
                if (fieldValue != null) {
                    method.invoke(protoBuilder, fieldValue);
                }
            }
        }
    }
}
