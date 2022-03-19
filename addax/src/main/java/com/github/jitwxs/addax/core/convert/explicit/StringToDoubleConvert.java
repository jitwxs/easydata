package com.github.jitwxs.addax.core.convert.explicit;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 21:23
 */
public class StringToDoubleConvert implements ExplicitConvert<String, Double> {
    @Override
    public Double convert(String input) {
        return Double.parseDouble(input);
    }
}