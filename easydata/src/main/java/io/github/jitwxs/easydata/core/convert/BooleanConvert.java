package io.github.jitwxs.easydata.core.convert;

import org.apache.commons.lang3.BooleanUtils;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class BooleanConvert implements IConvert<Boolean> {
    @Override
    public Boolean convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass == String.class) {
            return fromString((String) source);
        }

        return fromString(provider().convert(source, String.class));
    }

    @Override
    public Boolean convert(Object source, Class<?> target) {
        return null;
    }

    private Boolean fromString(String str) {
        return BooleanUtils.toBoolean(str);
    }
}
