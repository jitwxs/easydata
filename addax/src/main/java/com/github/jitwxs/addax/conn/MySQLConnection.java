package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.common.enums.DataTypeEnum;
import com.github.jitwxs.addax.common.exception.AddaxLoaderException;
import com.github.jitwxs.addax.common.util.LoadingUtils;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

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
    public Optional<Pair<DataTypeEnum, List<String[]>>> loading(LoaderProperties properties) {
        try {
            final List<String[]> list = LoadingUtils.loadSql(this, properties.url().getSqlConn());
            return Optional.of(Pair.of(DataTypeEnum.MATRIX, list));
        } catch (ClassNotFoundException | SQLException e) {
            throw new AddaxLoaderException(e);
        }
    }
}
