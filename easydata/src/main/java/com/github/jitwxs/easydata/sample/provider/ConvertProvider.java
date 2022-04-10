package com.github.jitwxs.easydata.sample.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.jitwxs.easydata.sample.common.exception.EasyDataConvertException;
import com.github.jitwxs.easydata.sample.common.util.ObjectUtils;
import com.github.jitwxs.easydata.sample.common.util.ProtobufUtils;
import com.github.jitwxs.easydata.sample.common.util.ReflectionUtils;
import com.github.jitwxs.easydata.sample.core.convert.IConvert;
import com.github.jitwxs.easydata.sample.core.convert.explicit.ExplicitConvert;
import com.github.jitwxs.easydata.sample.core.convert.implicit.ImplicitConvert;
import com.google.protobuf.Message;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 提供 {@link IConvert} 统一获取门面
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 20:36
 */
public class ConvertProvider extends Provider<IConvert, Class<?>> {
    @Override
    protected List<IConvert> loadNative() {
        final List<IConvert> resultList = new ArrayList<>();

        new Reflections(IConvert.class.getPackage().getName()).getSubTypesOf(IConvert.class).forEach(c -> {
            if (Modifier.isInterface(c.getModifiers())) {
                return;
            }
            resultList.add(ObjectUtils.create(c));
        });

        return resultList;
    }

    @Override
    protected Object uniqueKeyByInstance(IConvert instance) {
        final Class<? extends IConvert> clazz = instance.getClass();

        final Type[] arguments = ReflectionUtils.getGenericInterface0Class(clazz);

        if (arguments == null || arguments.length != 2) {
            throw new EasyDataConvertException("Illegal uniqueKeyByInstance() params");
        }

        return arguments[0] + "_" + arguments[1];
    }

    @Override
    protected Object uniqueKey(Class<?>... args) {
        if (args == null || args.length != 2) {
            throw new EasyDataConvertException("Illegal uniqueKeyByInstance() params");
        }

        return args[0] + "_" + args[1];
    }

    @SuppressWarnings("unchecked")
    public <T> T convert(Object source, Class<T> targetClass) {
        // 空对象
        if (source == null) {
            return null;
        }

        // string 类型空对象
        if (source instanceof String) {
            final String e = (String) source;
            if ("null".equals(e) || StringUtils.isBlank(e)) {
                return null;
            }
        }

        // 统一使用包装类型处理
        final Class<?> sourceClass = ClassUtils.primitiveToWrapper(source.getClass());
        targetClass = (Class<T>) ClassUtils.primitiveToWrapper(targetClass);

        // 类型相同，执行返回
        if (sourceClass == targetClass) {
            return (T) source;
        }

        // 1、直接类型路由
        IConvert iConvert = delegate(sourceClass, targetClass);
        if (iConvert != null) {
            return convert0(source, targetClass, iConvert);
        }

        // 2、使用父类型路由
        final List<Class<?>> inputList = new LinkedList<>();
        inputList.add(sourceClass);
        ReflectionUtils.loadSuperAndInterfaceClass(sourceClass, inputList);

        final List<Class<?>> targetList = new LinkedList<>();
        targetList.add(targetClass);
        ReflectionUtils.loadSuperAndInterfaceClass(targetClass, targetList);

        for (Class<?> input : inputList) {
            if (input == Serializable.class) {
                break;
            }

            for (Class<?> target : targetList) {
                if (target == Serializable.class) {
                    break;
                }

                iConvert = delegate(input, target);
                if (iConvert != null) {
                    return convert0(source, targetClass, iConvert);
                }
            }
        }

        // 4、使用 json 兜底
        return serialization(source, targetClass);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> T convert0(Object source, Class<T> targetClass, IConvert convert) {
        if (source.getClass() == targetClass) {
            return (T) source;
        }

        if (convert instanceof ExplicitConvert) {
            return (T) ((ExplicitConvert) convert).convert(source);
        }

        if (convert instanceof ImplicitConvert) {
            return (T) ((ImplicitConvert) convert).convert(source, targetClass);
        }

        throw new EasyDataConvertException("TypeConvertProvider DoConvert unknown: " + convert.getClass());
    }

    private static <T> T serialization(Object source, Class<T> targetClass) {
        final String json;
        if (source instanceof Message) {
            json = ProtobufUtils.toJson((Message) source);
        } else {
            json = JSON.toJSONString(source);
        }

        if (Message.class.isAssignableFrom(targetClass)) {
            return ProtobufUtils.toBean(json, targetClass);
        } else {
            return JSONObject.parseObject(json, targetClass);
        }
    }
}
