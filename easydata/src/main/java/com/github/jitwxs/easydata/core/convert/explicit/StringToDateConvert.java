package com.github.jitwxs.easydata.core.convert.explicit;

import com.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class StringToDateConvert implements ExplicitConvert<String, Date> {
    @Override
    public Date convert(String input) {
        try {
            String parse = input.replaceFirst("[0-9]{4}([^0-9]?)", "yyyy$1");
            parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
            parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
            parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");
            return new SimpleDateFormat(parse).parse(input);
        } catch (Exception e) {
            throw new EasyDataConvertException(e);
        }
    }
}