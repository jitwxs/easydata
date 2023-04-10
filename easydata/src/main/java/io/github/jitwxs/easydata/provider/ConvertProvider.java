package io.github.jitwxs.easydata.provider;

import com.google.common.base.Defaults;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.common.util.ReflectionUtils;
import io.github.jitwxs.easydata.core.convert.IConvert;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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
            resultList.add(ObjectUtils.create(c, null));
        });

        return resultList;
    }

    @Override
    protected List<Object> uniqueKeyByInstance(IConvert instance) {
        final Class<? extends IConvert> clazz = instance.getClass();

        final Type[] arguments = ReflectionUtils.getGenericInterface0Class(clazz);

        if (arguments == null || arguments.length != 1) {
            throw new EasyDataConvertException("Illegal uniqueKeyByInstance() params");
        }

        final Class<?> targetClass = (Class<?>) arguments[0];

        return Collections.singletonList(buildUniqueKey(targetClass));
    }

    @Override
    protected Object uniqueKey(Class<?>... args) {
        if (args == null || args.length != 1) {
            throw new EasyDataConvertException("Illegal uniqueKeyByInstance() params");
        }

        return this.buildUniqueKey(args[0]);
    }

    @SuppressWarnings("unchecked")
    public <T> T convert(Object source, Class<T> targetClass) {
        final T result = this.convert0(source, targetClass);

        return processFieldValue(result, targetClass);
    }

    private <T> T convert0(Object source, Class<T> targetClass) {
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
        IConvert iConvert = delegate(targetClass);
        if (iConvert != null) {
            return convert1(source, null, iConvert);
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

                iConvert = delegate(target);
                if (iConvert != null) {
                    return convert1(source, targetClass, iConvert);
                }
            }
        }

        // 4、兜底
        return convert1(source, targetClass, delegate(Object.class));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> T convert1(Object source, Class<T> actualTargetClass, IConvert convert) {
        return (T) (actualTargetClass == null ? convert.convert(source) : convert.convert(source, actualTargetClass));
    }

    /**
     * 对字段值进行处理
     *
     * @param fieldValue 字段值
     * @param fieldClass 字段类型
     * @return 经过处理后的字段值
     */
    private static <T> T processFieldValue(final T fieldValue, final Class<?> fieldClass) {
        // 对于基本数据类型，当设置为 null 时，需要转成默认值
        if (fieldClass.isPrimitive() && fieldValue == null) {
            return (T) Defaults.defaultValue(fieldClass);
        }

        return fieldValue;
    }

    private Object buildUniqueKey(Class<?> type1) {
        return type1;
    }
}
