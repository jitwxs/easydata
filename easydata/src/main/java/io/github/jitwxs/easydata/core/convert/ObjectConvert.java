package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.ClassGroupEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.function.ThrowableBiFunction;
import io.github.jitwxs.easydata.common.util.ObjectUtils;

import java.lang.reflect.Type;

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
            final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc = (name, type) -> {
                final FieldProperty property = PropertyCache.tryGet(source.getClass(), name);

                if (property != null && property.isReadable()) {
                    final Object fieldValue = property.getReadFunc().apply(source);
                    final Object value = provider().convert(fieldValue, (Class) type);

                    return value;
                } else {
                    return null;
                }
            };

            switch (ClassGroupEnum.delegate(target)) {
                case PROTOBUF_MESSAGE:
                    return ObjectUtils.createProtoMessage(target, null, fieldGeneratorFunc);
                case PROTOBUF_BUILDER:
                    return ObjectUtils.createProtoBuilder(target, null, fieldGeneratorFunc);
                case NATIVE:
                    return ObjectUtils.createJava(target, field -> false, null, fieldGeneratorFunc);
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (Throwable e) {
            throw new EasyDataMockException(e);
        }
    }
}
