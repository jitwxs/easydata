package io.github.jitwxs.easydata.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:27
 */
public class TimeUtils {
    public static long ldtToMs(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0L;
        }

        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime dateToLdt(final Date date) {
        if (date == null) {
            return null;
        }

        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime msToLdt(final long ms) {
        return dateToLdt(new Date(ms));
    }
}
