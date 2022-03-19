package com.github.jitwxs.addax.core.convert.explicit;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 21:23
 */
public class StringToBooleanConvert implements ExplicitConvert<String, Boolean> {
    @Override
    public Boolean convert(String input) {
        return Boolean.parseBoolean(input);
    }
}
