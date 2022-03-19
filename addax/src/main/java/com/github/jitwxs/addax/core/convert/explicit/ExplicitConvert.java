package com.github.jitwxs.addax.core.convert.explicit;

import com.github.jitwxs.addax.core.convert.IConvert;

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
     */
    T convert(I input);
}