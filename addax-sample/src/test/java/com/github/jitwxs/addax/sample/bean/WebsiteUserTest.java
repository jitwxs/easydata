package com.github.jitwxs.addax.sample.bean;

import com.github.jitwxs.addax.conn.MySQLConnection;
import com.github.jitwxs.addax.core.loader.Loader;
import com.github.jitwxs.addax.core.loader.LoaderProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
public class WebsiteUserTest {
    @Test
    public void loadCsv() {
        final LoaderProperties properties = LoaderProperties.builder().url("/addax/loader/website_user.csv").build();

        this.assert0(Loader.FILE_LOADER, properties);
    }

    @Test
    public void loadJson() {
        final LoaderProperties properties = LoaderProperties.builder().url("/addax/loader/website_user.json").build();

        this.assert0(Loader.FILE_LOADER, properties);
    }

    @Container
    private static final JdbcDatabaseContainer container = new MySQLContainer()
            .withDatabaseName("MOCK_DB")
            .withInitScript("addax/sql/website_user.sql");

    @Test
    public void loadMySQL() {
        final MySQLConnection connection = new MySQLConnection(
                container.getDriverClassName(),
                container.getUsername(),
                container.getPassword(),
                container.getJdbcUrl());

        final LoaderProperties properties = LoaderProperties.builder().url("SELECT * FROM website_user").build();

        this.assert0(new Loader(connection), properties);
    }

    private void assert0(final Loader loader, LoaderProperties properties) {
        final List<WebsiteUser> userList = loader.loading(WebsiteUser.class, properties);

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(5, userList.size());
    }
}