package com.github.jitwxs.easydata.sample.bean;

import com.github.jitwxs.easydata.conn.MySQLConnection;
import com.github.jitwxs.easydata.core.loader.EasyLoader;
import com.github.jitwxs.easydata.core.loader.LoaderProperties;
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
        final LoaderProperties properties = LoaderProperties.builder().url("/easydata/loader/website_user.csv").build();

        this.assert0(EasyLoader.FILE_EASY_LOADER, properties);
    }

    @Test
    public void loadJson() {
        final LoaderProperties properties = LoaderProperties.builder().url("/easydata/loader/website_user.json").build();

        this.assert0(EasyLoader.FILE_EASY_LOADER, properties);
    }

    @Container
    private static final JdbcDatabaseContainer container = new MySQLContainer()
            .withDatabaseName("MOCK_DB")
            .withInitScript("easydata/sql/website_user.sql");

    @Test
    public void loadMySQL() {
        final MySQLConnection connection = new MySQLConnection(
                container.getDriverClassName(),
                container.getUsername(),
                container.getPassword(),
                container.getJdbcUrl());

        final LoaderProperties properties = LoaderProperties.builder().url("SELECT * FROM website_user").build();

        this.assert0(new EasyLoader(connection), properties);
    }

    private void assert0(final EasyLoader easyLoader, LoaderProperties properties) {
        final List<WebsiteUser> userList = easyLoader.loading(WebsiteUser.class, properties);

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(5, userList.size());
    }
}