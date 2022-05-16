package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.util.TimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

        if (sourceClass == Date.class) {
            return TimeUtils.dateToLdt((Date) source);
        }

        if (sourceClass == LocalDate.class) {
            return TimeUtils.ldToLdt((LocalDate) source);
        }

        if (sourceClass == String.class) {
            final Date date = provider().convert(source, Date.class);
            return TimeUtils.dateToLdt(date);
        }

        return null;
    }

    @Override
    public LocalDateTime convert(Object source, Class<?> target) {
        return null;
    }
}
