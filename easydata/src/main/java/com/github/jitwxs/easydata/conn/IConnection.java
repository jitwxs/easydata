package com.github.jitwxs.easydata.conn;

import com.github.jitwxs.easydata.core.loader.LoadingSource;
import com.github.jitwxs.easydata.core.loader.LoaderProperties;

import java.util.Optional;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:35
 */
public interface IConnection {
    Optional<LoadingSource<?>> loading(LoaderProperties properties);
}
