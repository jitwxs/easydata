package io.github.jitwxs.easydata.core.convert;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class DoubleConvert implements IConvert<Double> {
    @Override
    public Double convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass == String.class) {
            return fromString((String) source);
        }

        return fromString(provider().convert(source, String.class));
    }

    @Override
    public Double convert(Object source, Class<?> target) {
        return null;
    }

    private Double fromString(String str) {
        return Double.parseDouble(str);
    }
}
