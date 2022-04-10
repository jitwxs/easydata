package com.github.jitwxs.easydata.sample.core.convert.implicit;

import com.github.jitwxs.easydata.sample.common.exception.EasyDataConvertException;
import com.github.jitwxs.easydata.sample.core.convert.IConvert;

/**
 * implicit convert any type to anytype
 *
 * @param <I> specify input class type
 * @param <P> specify output's parent class type
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 20:35
 */
public interface ImplicitConvert<I, P> extends IConvert {
    /**
     * @param source      input object
     * @param targetClass specify output class type
     * @param <T>         target type to be converted
     * @return convert result instance
     */
    <T> T convert(I source, Class<T> targetClass);

    default <T> void validParams(Class<T> targetClass, Class<P> targetParentClass) {
        if (!targetParentClass.isAssignableFrom(targetClass)) {
            throw new EasyDataConvertException(String.format("ImplicitTypeConvert Not Support, target: %s, parent: %s", targetClass, targetParentClass));
        }
    }
}
