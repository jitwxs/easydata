package io.github.jitwxs.easydata.sample.sample.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:27
 */
public class TimeUtils {
    public static long ldtToMs(final LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime).getTime();
    }

    public static LocalDateTime dateToLdt(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
