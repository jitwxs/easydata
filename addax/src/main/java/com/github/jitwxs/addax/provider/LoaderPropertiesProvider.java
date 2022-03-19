package com.github.jitwxs.addax.provider;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.common.exception.AddaxLoaderException;

import java.util.List;

/**
 * 加载器配置 provider
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
            throw new AddaxLoaderException("Illegal uniqueKey() params");
        }

        return args[0];
    }
}
