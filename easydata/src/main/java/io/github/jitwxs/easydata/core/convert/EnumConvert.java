package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.cache.EnumCache;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 15:36
 */
public class EnumConvert implements IConvert<Enum> {
    @Override
    public Enum convert(Object source) throws EasyDataConvertException {
        throw new EasyDataConvertException("Not support operation");
    }

    @Override
    public Enum convert(Object source, Class<?> target) throws EasyDataConvertException {
        final Class<?> sourceClass = source.getClass();

        if (source instanceof Enum) {
            final Enum input = (Enum) source;

            final EnumCache.EnumProperty property = EnumCache.tryGet(target);
            if (property == null) {
                return null;
            }

            Enum one = property.getByName(input.name());

            if (one == null) {
                one = property.getIdMap().get(input.ordinal());
            }

            return one;
        }

        if (sourceClass == String.class) {
            final String input = (String) source;

            if (NumberUtils.isParsable(input)) {
                final Integer number = provider().convert(input, Integer.class);

                return EnumCache.tryGet(target).getIdMap().get(number);
            } else {
                return EnumCache.tryGet(target).getByName(input);
            }
        }

        return null;
    }
}
