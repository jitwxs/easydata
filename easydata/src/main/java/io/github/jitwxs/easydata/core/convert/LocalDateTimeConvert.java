package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.util.TimeUtils;

import java.time.LocalDateTime;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class LocalDateTimeConvert implements IConvert<LocalDateTime> {
    @Override
    public LocalDateTime convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass == Long.class) {
            return TimeUtils.msToLdt((Long) source);
        }

        return null;
    }

    @Override
    public LocalDateTime convert(Object source, Class<?> target) {
        return null;
    }
}
