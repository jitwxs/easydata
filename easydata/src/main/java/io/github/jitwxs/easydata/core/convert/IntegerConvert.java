package io.github.jitwxs.easydata.core.convert;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class IntegerConvert implements IConvert<Integer> {
    @Override
    public Integer convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass.isEnum()) {
            return ((Enum) source).ordinal();
        }

        if (sourceClass == String.class) {
            return fromString((String) source);
        }

        return fromString(provider().convert(source, String.class));
    }

    @Override
    public Integer convert(Object source, Class<?> target) {
        return null;
    }

    private Integer fromString(String str) {
        return Integer.parseInt(str);
    }
}
