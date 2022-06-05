package io.github.jitwxs.easydata.conn;

import io.github.jitwxs.easydata.core.loader.LoadingSource;
import io.github.jitwxs.easydata.common.bean.MatrixBean;
import io.github.jitwxs.easydata.core.loader.MatrixLoadingSource;
import io.github.jitwxs.easydata.common.exception.EasyDataLoaderException;
import io.github.jitwxs.easydata.common.util.LoadingUtils;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 基于 MySQL 的连接
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:36
 */
@Getter
@AllArgsConstructor
public class MySQLConnection implements IConnection {
    /**
     * 驱动名
     */
    private final String driverName;

    /**
     * 连接用户名
     */
    private final String username;

    /**
     * 密码
     */
    private final String password;

    /**
     * jdbc 连接 url
     */
    private final String jdbcUrl;

    @Override
    public Optional<LoadingSource<?>> loading(LoaderProperties properties) {
        try {
            final List<String[]> list = LoadingUtils.loadSql(this, properties.getUrl());

            return Optional.of(new MatrixLoadingSource(properties, MatrixBean.newInstance(list)));
        } catch (ClassNotFoundException | SQLException e) {
            throw new EasyDataLoaderException(e);
        }
    }
}
