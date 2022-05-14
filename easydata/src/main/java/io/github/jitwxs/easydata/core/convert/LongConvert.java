package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.util.TimeUtils;

import java.time.LocalDateTime;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class LongConvert implements IConvert<Long> {
    @Override
    public Long convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (source == String.class) {
            return Long.parseLong((String) source);
        }

        if (sourceClass == LocalDateTime.class) {
            return TimeUtils.ldtToMs((LocalDateTime) source);
        }

        return null;
    }

    @Override
    public Long convert(Object source, Class<?> target) {
        return null;
    }
}
