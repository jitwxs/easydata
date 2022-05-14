package io.github.jitwxs.easydata.core.convert;

import com.google.protobuf.Message;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.function.ThrowableBiFunction;
import io.github.jitwxs.easydata.common.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 15:31
 */
public class ObjectConvert implements IConvert<Object> {

    @Override
    public Object convert(Object source) throws EasyDataConvertException {
        return source;
    }

    @Override
    public Object convert(Object source, Class<?> target) throws EasyDataConvertException {
        try {
            final Map<String, PropertyDescriptor> descriptorMap = PropertyCache.tryGet(source.getClass()).getReadable();

            final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc = (name, type) -> {
                final PropertyDescriptor descriptor = descriptorMap.get(name);

                if (descriptor == null) {
                    return null;
                } else {
                    final Object fieldValue = descriptor.getReadMethod().invoke(source);

                    return provider().convert(fieldValue, (Class) type);
                }
            };

            if (Message.class.isAssignableFrom(target)) {
                return ObjectUtils.createProto(target, fieldGeneratorFunc);
            } else {
                return ObjectUtils.createJava(target, field -> false, fieldGeneratorFunc);
            }
        } catch (Throwable e) {
            throw new EasyDataMockException(e);
        }
    }
}
