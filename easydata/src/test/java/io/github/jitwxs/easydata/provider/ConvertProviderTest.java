package io.github.jitwxs.easydata.provider;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ConvertProviderTest {
    @Test
    public void testConvert() {
        final ConvertProvider provider = ProviderFactory.delegate(ConvertProvider.class);

        final Boolean aBoolean = provider.convert("true", boolean.class);
        assertTrue(aBoolean);

        final Date date = provider.convert("1649809682988", Date.class);
        assertEquals(1649809682988L, date.getTime());

        final BigDecimal bigDecimal = provider.convert("15.666", BigDecimal.class);
        assertEquals(0, bigDecimal.compareTo(new BigDecimal("15.666")));
    }
}