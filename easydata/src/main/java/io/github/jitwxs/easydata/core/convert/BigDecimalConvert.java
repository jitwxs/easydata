package io.github.jitwxs.easydata.core.convert;

import java.math.BigDecimal;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class BigDecimalConvert implements IConvert<BigDecimal> {
    @Override
    public BigDecimal convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass == String.class) {
            return fromString((String) source);
        }

        return fromString(provider().convert(source, String.class));
    }

    @Override
    public BigDecimal convert(Object source, Class<?> target) {
        return null;
    }

    private BigDecimal fromString(String value) {
        return new BigDecimal(value);
    }
}
