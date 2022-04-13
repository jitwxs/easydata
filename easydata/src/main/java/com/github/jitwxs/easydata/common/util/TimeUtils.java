package com.github.jitwxs.easydata.common.util;

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
        return localDateTime.toInstant(ZoneOffset.of("+8")).getEpochSecond() * 1000;
    }

    public static LocalDateTime dateToLdt(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}