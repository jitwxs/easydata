package io.github.jitwxs.easydata.core.convert.explicit;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 21:23
 */
public class StringToIntegerConvert implements ExplicitConvert<String, Integer> {
    @Override
    public Integer convert(String input) {
        return Integer.parseInt(input);
    }
}