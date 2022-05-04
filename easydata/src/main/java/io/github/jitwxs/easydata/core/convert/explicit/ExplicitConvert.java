package io.github.jitwxs.easydata.core.convert.explicit;

import io.github.jitwxs.easydata.core.convert.IConvert;

/**
 * explicit convert any type to anytype
 *
 * @param <I> specify input class type
 * @param <T> specify output class type
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 20:34
 */
public interface ExplicitConvert<I, T> extends IConvert {
    /**
     * convert implement
     *
     * @param input 输入对象
     * @return 经过转换后的输出对象
     */
    T convert(I input);
}