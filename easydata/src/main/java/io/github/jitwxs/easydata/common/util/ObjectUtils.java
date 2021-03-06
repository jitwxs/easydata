package io.github.jitwxs.easydata.common.util;

import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.ClassGroupEnum;
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
import java.util.function.Consumer;

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
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> target) {
        try {
            // 基于构造方法
            return delegate.create(target);
        } catch (ReflectionException e) {
            // 基于 builder 构造器
            final Object builder = createBuilder(target, ClassGroupEnum.Group.NATIVE);
            if (builder != null) {
                return (T) buildBuilder(builder);
            }
        }

        return null;
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
                                   final ThrowableFunction<Field, Boolean> fieldIgnoreGeneratorFunc,
                                   final Consumer<Object> newInstanceConsume,
                                   final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc) throws Throwable {
        final Object invoke = ObjectUtils.create(target);

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

            final Object fieldValue = fieldGeneratorFunc.apply(fieldName, field.getGenericType());
            if (fieldValue != null) {
                property.getWriteFunc().apply(invoke, fieldValue);
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
    @SuppressWarnings("unchecked")
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
        final T invoke = ObjectUtils.create(target);

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

            final String writeMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method method = null;
            try {
                method = protoBuilderClass.getDeclaredMethod(writeMethodName, entry.getValue().getTarget());
            } catch (NoSuchMethodException e) {
                log.error("ObjectUtils fillingProtoBuilderField error, protoMessageClass: {}, field: {}", protoMessageClass, fieldName);
            }

            if (method != null) {
                final Object fieldValue = fieldGeneratorFunc.apply(fieldName, entry.getValue().getType());
                if (fieldValue != null) {
                    method.invoke(protoBuilder, fieldValue);
                }
            }
        }
    }
}
