package com.github.jitwxs.addax.sample.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:27
 */
public class TimeUtils {
    public static long ldtToMs(final LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).getEpochSecond();
    }

    public static LocalDateTime dateToLdt(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
