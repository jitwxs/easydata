package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.util.TimeUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDateTime;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class LongConvert implements IConvert<Long> {
    @Override
    public Long convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass == String.class) {
            final String target = (String) source;

            if (NumberUtils.isParsable(target)) {
                return Long.parseLong(target);
            }
        } else if (sourceClass == Integer.class || source == Short.class) {
            return Long.parseLong(String.valueOf(source));
        }

        final LocalDateTime ldt = provider().convert(source, LocalDateTime.class);

        return TimeUtils.ldtToMs(ldt);
    }

    @Override
    public Long convert(Object source, Class<?> target) {
        return null;
    }
}
