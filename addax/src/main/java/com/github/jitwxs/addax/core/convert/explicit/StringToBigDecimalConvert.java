package com.github.jitwxs.addax.core.convert.explicit;

import java.math.BigDecimal;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 21:23
 */
public class StringToBigDecimalConvert implements ExplicitConvert<String, BigDecimal> {
    @Override
    public BigDecimal convert(String input) {
        return new BigDecimal(input);
    }
}
