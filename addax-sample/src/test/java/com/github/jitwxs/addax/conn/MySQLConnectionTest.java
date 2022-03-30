package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * dependency docker environment 🐳
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 0:04
 */
@Testcontainers
public class MySQLConnectionTest extends BaseConnectionTest {
    @Container
    private static final JdbcDatabaseContainer container = new MySQLContainer()
            .withDatabaseName("express")
            .withInitScript("addax/sql/express.sql");

    @Test
    public void loading() {
        final MySQLConnection connection = new MySQLConnection(
                container.getDriverClassName(),
                container.getUsername(),
                container.getPassword(),
                container.getJdbcUrl());

        assert0(connection, LoaderProperties.builder().url("SELECT * FROM order_evaluate").build());
    }
}
