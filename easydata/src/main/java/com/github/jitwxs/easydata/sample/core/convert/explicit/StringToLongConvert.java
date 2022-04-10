package com.github.jitwxs.easydata.sample.core.convert.explicit;

import com.github.jitwxs.easydata.sample.provider.ConvertProvider;
import com.github.jitwxs.easydata.sample.provider.ProviderFactory;

import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 21:23
 */
public class StringToLongConvert implements ExplicitConvert<String, Long> {
    @Override
    public Long convert(String input) {
        try {
            return Long.parseLong(input);
        } catch (Exception e) {
            // timestamp
            final Date date = ProviderFactory.delegate(ConvertProvider.class).convert(input, Date.class);
            return date == null ? 0L : date.getTime();
        }
    }
}