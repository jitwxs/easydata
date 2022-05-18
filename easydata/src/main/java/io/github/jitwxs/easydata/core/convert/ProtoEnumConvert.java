package io.github.jitwxs.easydata.core.convert;

import com.google.protobuf.ProtocolMessageEnum;
import io.github.jitwxs.easydata.common.cache.EnumCache;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 15:36
 */
public class ProtoEnumConvert implements IConvert<ProtocolMessageEnum> {
    @Override
    public ProtocolMessageEnum convert(Object source) throws EasyDataConvertException {
        throw new EasyDataConvertException("Not support operation");
    }

    @Override
    public ProtocolMessageEnum convert(Object source, Class<?> target) throws EasyDataConvertException {
        final Class<?> sourceClass = source.getClass();

        if (source instanceof Enum) {
            final Enum input = (Enum) source;

            return (ProtocolMessageEnum) EnumCache.tryGet(target, input.name(), input.ordinal());
        }

        if (sourceClass == String.class) {
            final String input = (String) source;

            if (NumberUtils.isParsable(input)) {
                final Integer number = provider().convert(input, Integer.class);

                return (ProtocolMessageEnum) EnumCache.tryGet(target).getIdMap().get(number);
            } else {
                return (ProtocolMessageEnum) EnumCache.tryGet(target).getNameMap().get(input);
            }
        }

        return null;
    }
}
