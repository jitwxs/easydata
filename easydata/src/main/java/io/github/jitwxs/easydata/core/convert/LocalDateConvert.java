package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.util.TimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class LocalDateConvert implements IConvert<LocalDate> {
    @Override
    public LocalDate convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass == Long.class) {
            return fromLong((Long) source);
        }

        final LocalDateTime ldt = provider().convert(source, LocalDateTime.class);

        return ldt.toLocalDate();
    }

    @Override
    public LocalDate convert(Object source, Class<?> target) {
        return null;
    }

    public LocalDate fromLong(final Long ms) {
        return fromLocalDateTime(TimeUtils.msToLdt(ms));
    }

    public LocalDate fromLocalDateTime(final LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }
}
