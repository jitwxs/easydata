package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.common.enums.DataTypeEnum;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:35
 */
public interface IConnection {
    Optional<Pair<DataTypeEnum, List<String[]>>> loading(LoaderProperties properties);
}
