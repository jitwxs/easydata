package com.github.jitwxs.addax.config;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.github.jitwxs.addax.common.util.TimeUtils;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * fastjson 处理 {@link java.time.LocalDateTime} 反序列化
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 11:44
 */
public class LocalDateTimeDeSerializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object object) {
        final long value = parser.parseObject(long.class);

        if (value <= 0) {
            return null;
        }

        return (T) TimeUtils.dateToLdt(new Date(value));
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
