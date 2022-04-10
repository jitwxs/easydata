package com.github.jitwxs.easydata.sample.conn;

import com.github.jitwxs.easydata.sample.core.loader.LoadingSource;
import com.github.jitwxs.easydata.sample.common.bean.MatrixBean;
import com.github.jitwxs.easydata.sample.core.loader.MatrixLoadingSource;
import com.github.jitwxs.easydata.sample.common.exception.EasyDataLoaderException;
import com.github.jitwxs.easydata.sample.common.util.LoadingUtils;
import com.github.jitwxs.easydata.sample.core.loader.LoaderProperties;
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
            return Optional.of(new MatrixLoadingSource(properties, new MatrixBean(list)));
        } catch (ClassNotFoundException | SQLException e) {
            throw new EasyDataLoaderException(e);
        }
    }
}