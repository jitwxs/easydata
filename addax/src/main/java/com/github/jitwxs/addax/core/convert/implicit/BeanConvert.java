package com.github.jitwxs.addax.core.convert.implicit;

import com.github.jitwxs.addax.common.cache.PropertyCache;
import com.github.jitwxs.addax.common.exception.AddaxConvertException;
import com.github.jitwxs.addax.common.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 21:23
 */
public class BeanConvert implements ImplicitConvert<Object, Object> {

    @Override
    public <T> T convert(Object source, Class<T> targetClass) {
        final T target = ObjectUtils.create(targetClass);

        final Map<String, PropertyDescriptor> sourcePropertyMap = PropertyCache.tryGet(source.getClass()).getReadable();
        final Map<String, PropertyDescriptor> targetPropertyMap = PropertyCache.tryGet(targetClass).getWriteAble();

        for (Map.Entry<String, PropertyDescriptor> entry : sourcePropertyMap.entrySet()) {
            final Method readMethod = entry.getValue().getReadMethod();
            final PropertyDescriptor targetDescriptor = targetPropertyMap.get(entry.getKey());

            if (targetDescriptor != null) {
                final Method writeMethod = targetDescriptor.getWriteMethod();
                if (checkArgMatchInReadWriteMethod(readMethod.getReturnType(), writeMethod.getParameterTypes())) {
                    readMethod.setAccessible(true);
                    writeMethod.setAccessible(true);
                   try {
                       writeMethod.invoke(target, readMethod.invoke(source));
                   } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                       throw new AddaxConvertException(e);
                   }
                }
            }
        }

        return target;
    }

    /**
     * check read method return type equals write method param type
     *
     * @param readMethodReturnType read method return type
     * @param writeMethodArgsType  rite method params type
     * @return return true if matched, or else return false
     */
    private static boolean checkArgMatchInReadWriteMethod(final Class<?> readMethodReturnType, final Class<?>[] writeMethodArgsType) {
        if (writeMethodArgsType == null || writeMethodArgsType.length != 1) {
            return false;
        }
        return readMethodReturnType == writeMethodArgsType[0];
    }
}