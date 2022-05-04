package io.github.jitwxs.easydata.core.convert.explicit;

import io.github.jitwxs.easydata.common.util.TimeUtils;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
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