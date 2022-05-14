package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;

/**
 *
 * @author jitwxs@foxmail.com
 * @since 2022-05-14 14:48
 */
public interface IConvert<T> {

    T convert(final Object source) throws EasyDataConvertException;

    T convert(final Object source, final Class<?> target) throws EasyDataConvertException;

    default ConvertProvider provider() {
        return ProviderFactory.delegate(ConvertProvider.class);
    }
}
