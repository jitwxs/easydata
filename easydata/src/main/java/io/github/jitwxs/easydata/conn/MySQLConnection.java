package io.github.jitwxs.easydata.conn;

import io.github.jitwxs.easydata.core.loader.LoadingSource;
import io.github.jitwxs.easydata.common.bean.MatrixBean;
import io.github.jitwxs.easydata.core.loader.MatrixLoadingSource;
import io.github.jitwxs.easydata.common.exception.EasyDataLoaderException;
import io.github.jitwxs.easydata.common.util.LoadingUtils;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
import lombok.Getter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:36
 */
@Getter
public class MySQLConnection implements IConnection {
    private final String driverName;

    private final String username;

    private final String password;

    private final String jdbcUrl;

    public MySQLConnection(String driverName, String username, String password, String jdbcUrl) {
        this.driverName = driverName;
        this.username = username;
        this.password = password;
        this.jdbcUrl = jdbcUrl;
    }

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
