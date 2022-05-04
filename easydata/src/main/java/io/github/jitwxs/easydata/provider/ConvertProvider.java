package io.github.jitwxs.easydata.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.common.util.ProtobufUtils;
import io.github.jitwxs.easydata.common.util.ReflectionUtils;
import io.github.jitwxs.easydata.core.convert.IConvert;
import io.github.jitwxs.easydata.core.convert.explicit.ExplicitConvert;
import io.github.jitwxs.easydata.core.convert.implicit.ImplicitConvert;
import com.google.protobuf.Message;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.ClassUtils.*;

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
    protected List<Object> uniqueKeyByInstance(IConvert instance) {
        final Class<? extends IConvert> clazz = instance.getClass();

        final Type[] arguments = ReflectionUtils.getGenericInterface0Class(clazz);

        if (arguments == null || arguments.length != 2) {
            throw new EasyDataConvertException("Illegal uniqueKeyByInstance() params");
        }

        final Class<?> sourceClass = (Class<?>) arguments[0], targetClass = (Class<?>) arguments[1];

        final List<Object> keyList = new ArrayList<>();

        keyList.add(buildUniqueKey(sourceClass, targetClass));

        int wrapperCount = 0;
        if (isPrimitiveWrapper(sourceClass)) {
            keyList.add(buildUniqueKey(wrapperToPrimitive(sourceClass), targetClass));
            wrapperCount++;
        }
        if (isPrimitiveWrapper(targetClass)) {
            keyList.add(buildUniqueKey(sourceClass, wrapperToPrimitive(targetClass)));
            wrapperCount++;
        }
        if (wrapperCount == 2) {
            keyList.add(buildUniqueKey(wrapperToPrimitive(sourceClass), wrapperToPrimitive(targetClass)));
        }

        return keyList;
    }

    @Override
    protected Object uniqueKey(Class<?>... args) {
        if (args == null || args.length != 2) {
            throw new EasyDataConvertException("Illegal uniqueKeyByInstance() params");
        }

        return this.buildUniqueKey(args[0], args[1]);
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
        final Class<?> sourceClass = primitiveToWrapper(source.getClass());
        targetClass = (Class<T>) primitiveToWrapper(targetClass);

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

    private Object buildUniqueKey(Class<?> type1, Class<?> type2) {
        return type1 + "_" + type2;
    }
}
