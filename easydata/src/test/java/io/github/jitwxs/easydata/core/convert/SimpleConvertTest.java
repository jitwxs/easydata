package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-29 14:57
 */
public class SimpleConvertTest {
    @Test
    public void stringToObject() {
        final ConvertProvider delegate = ProviderFactory.delegate(ConvertProvider.class);

        // 字符串转数值
        assertEquals(111, delegate.convert("111", int.class));
        assertEquals(12.5, delegate.convert("12.5", double.class));

        // 字符串转 boolean
        assertEquals(true, delegate.convert("true", boolean.class));
        assertEquals(false, delegate.convert("0", boolean.class));
        assertEquals(true, delegate.convert("T", boolean.class));

        // 时间转换
        assertEquals(LocalDateTime.of(2022, 5, 29, 15, 1, 3, 0),
                delegate.convert("2022-05-29 15:01:03.000", LocalDateTime.class));
        assertEquals(LocalDateTime.of(2022, 5, 29, 15, 0, 49, 921 * 1000000),
                delegate.convert(1653807649921L, LocalDateTime.class));
    }
}
