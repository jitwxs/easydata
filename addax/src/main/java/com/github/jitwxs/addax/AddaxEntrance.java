package com.github.jitwxs.addax;

import com.alibaba.fastjson.parser.ParserConfig;
import com.github.jitwxs.addax.config.LocalDateTimeDeSerializer;

import java.time.LocalDateTime;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 11:42
 */
public class AddaxEntrance {
    static {
        initialFastJsonConfig();
    }

    private static void initialFastJsonConfig() {
        final ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        parserConfig.putDeserializer(LocalDateTime.class, new LocalDateTimeDeSerializer());
    }
}
