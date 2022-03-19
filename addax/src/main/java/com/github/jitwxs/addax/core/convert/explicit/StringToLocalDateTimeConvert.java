package com.github.jitwxs.addax.core.convert.explicit;

import com.github.jitwxs.addax.provider.ConvertProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class StringToLocalDateTimeConvert implements ExplicitConvert<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String input) {
        final Date date = ProviderFactory.delegate(ConvertProvider.class).convert(input, Date.class);

        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}