package com.github.jitwxs.addax.provider;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.common.exception.AddaxLoaderException;

import java.util.Arrays;
import java.util.List;

/**
 * 提供 {@link LoaderProperties} 统一获取门面
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:53
 */
public class LoaderPropertiesProvider extends Provider<LoaderProperties> {

    @Override
    protected List<LoaderProperties> loadNative() {
        return null;
    }

    @Override
    protected Object uniqueKeyByInstance(LoaderProperties instance) {
        return instance.clazz();
    }

    @Override
    protected Object uniqueKey(Class<?>... args) {
        if (args == null || args.length != 1) {
            throw new AddaxLoaderException("Illegal uniqueKey() params: " + Arrays.toString(args));
        }

        return args[0];
    }
}
