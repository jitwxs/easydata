package com.github.jitwxs.easydata;

import com.alibaba.fastjson.parser.ParserConfig;
import com.github.jitwxs.easydata.config.LocalDateTimeDeSerializer;

import java.time.LocalDateTime;

/**
 * 项目入口类
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 11:42
 */
public class EasyDataEntrance {
    static {
        initialFastJsonConfig();
    }

    private static void initialFastJsonConfig() {
        final ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        parserConfig.putDeserializer(LocalDateTime.class, new LocalDateTimeDeSerializer());
    }
}
