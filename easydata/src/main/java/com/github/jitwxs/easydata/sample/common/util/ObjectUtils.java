package com.github.jitwxs.easydata.sample.common.util;

import com.github.jitwxs.easydata.sample.common.exception.EasyDataException;
import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.List;

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
}
