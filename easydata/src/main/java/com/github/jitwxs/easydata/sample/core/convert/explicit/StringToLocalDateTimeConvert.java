package com.github.jitwxs.easydata.sample.core.convert.explicit;

import com.github.jitwxs.easydata.sample.common.util.TimeUtils;
import com.github.jitwxs.easydata.sample.provider.ConvertProvider;
import com.github.jitwxs.easydata.sample.provider.ProviderFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class StringToLocalDateTimeConvert implements ExplicitConvert<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String input) {
        final Date date = ProviderFactory.delegate(ConvertProvider.class).convert(input, Date.class);

        return TimeUtils.dateToLdt(date);
    }
}