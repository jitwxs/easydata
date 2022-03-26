package com.github.jitwxs.addax.provider;

import com.github.jitwxs.addax.common.exception.AddaxLoaderException;
import com.github.jitwxs.addax.core.loader.LoaderProperties;

import java.util.Arrays;
import java.util.List;

/**
 * 提供 {@link LoaderProperties} 统一获取门面
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:53
 */
public class LoaderPropertiesProvider extends Provider<LoaderProperties, Class<?>> {

    @Override
    protected List<LoaderProperties> loadNative() {
        return null;
    }

    @Override
    protected Object uniqueKeyByInstance(LoaderProperties instance) {
        return instance.getClazz();
    }

    @Override
    protected Object uniqueKey(Class<?>... args) {
        if (args == null || args.length != 1) {
            throw new AddaxLoaderException("Illegal uniqueKey() params: " + Arrays.toString(args));
        }

        return args[0];
    }

    @Override
    protected List<LoaderProperties> loadSpi(ClassLoader classLoader, Class<LoaderProperties> loadClass) {
        // not support spi register
        return null;
    }

    public static void register(final LoaderProperties... properties) {
        if (properties != null && properties.length > 0) {
            final LoaderPropertiesProvider provider = ProviderFactory.delegate(LoaderPropertiesProvider.class);
            provider.doRegister.accept(Arrays.asList(properties));
        }
    }
}
