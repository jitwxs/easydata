package io.github.jitwxs.easydata.conn;

import io.github.jitwxs.easydata.core.loader.LoadingSource;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;

import java.util.Optional;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:35
 */
public interface IConnection {
    Optional<LoadingSource<?>> loading(LoaderProperties properties);
}
