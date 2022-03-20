package com.github.jitwxs.addax.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.powermock.reflect.Whitebox;

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
     * @param type 类型
     */
    public static <T> T create(Class<T> type) {
        try {
            return delegate.create(type);
        } catch (ReflectionException e) {
            log.error("ObjectUtils create error, type: {}", type, e);
            return null;
        }
    }

    /**
     * 根据 class 创建对象，指定构造方法
     *
     * @param type                类型
     * @param constructorArgTypes 构造方法参数类型
     * @param constructorArgs     构造方法参数
     */
    public static <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return delegate.create(type, constructorArgTypes, constructorArgs);
    }
}
