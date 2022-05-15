package io.github.jitwxs.easydata.core.convert;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:52
 */
public class StringConvert implements IConvert<String> {
    @Override
    public String convert(Object source) {
        final Class<?> sourceClass = source.getClass();

        if (sourceClass.isEnum()) {
            return ((Enum) source).name();
        }

        return String.valueOf(source);
    }

    @Override
    public String convert(Object source, Class<?> target) {
        return null;
    }
}
