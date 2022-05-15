package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class DateConvert implements IConvert<Date> {
    @Override
    public Date convert(Object source) throws EasyDataConvertException {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass == Long.class) {
            return new Date((Long) source);
        }

        if (sourceClass == String.class) {
            final String input = (String) source;

            // 使用 timestamp
            if (StringUtils.isNumeric(input)) {
                return new Date(Long.parseLong(input));
            }

            // 使用 format 转换
            String parse = input.replaceFirst("[0-9]{4}([^0-9]?)", "yyyy$1");
            parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
            parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");

            try {
                return new SimpleDateFormat(parse).parse(input);
            } catch (ParseException e) {
                throw new EasyDataConvertException(e);
            }
        }

        return null;
    }

    @Override
    public Date convert(Object source, Class<?> target) {
        return null;
    }
}
