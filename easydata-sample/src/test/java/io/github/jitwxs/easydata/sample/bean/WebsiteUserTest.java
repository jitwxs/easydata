package io.github.jitwxs.easydata.sample.bean;

import io.github.jitwxs.easydata.common.enums.FileFormatEnum;
import io.github.jitwxs.easydata.conn.EmbeddedConnection;
import io.github.jitwxs.easydata.conn.MySQLConnection;
import io.github.jitwxs.easydata.core.loader.EasyLoader;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
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

        this.assert0(EasyLoader.FILE_LOADER, properties);
    }

    @Test
    public void loadEmbeddedCsv() {
        final EmbeddedConnection connection = new EmbeddedConnection(FileFormatEnum.CSV);
        final EasyLoader easyLoader = new EasyLoader(connection);

        final LoaderProperties properties = LoaderProperties.builder().url("id,first_name,last_name,age,email,gender,ip_address,agent,create_time\n" +
                "1,Pepito,MacShane,2,pmacshane0@mashable.com,Male,229.152.71.123,\"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; zh-tw) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16\",2022-03-28 17:11:06\n" +
                "2,Con,Dine-Hart,48,cdinehart1@omniture.com,Female,232.143.248.181,Mozilla/5.0 (Windows NT 6.2; rv:21.0) Gecko/20130326 Firefox/21.0,2021-09-05 02:33:50\n" +
                "3,Amelita,Pennoni,77,apennoni2@state.gov,Female,76.91.68.251,\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36\",2021-10-28 10:13:10\n" +
                "4,Sheela,O'Shiel,94,soshiel3@arstechnica.com,Female,127.230.59.89,Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0,2021-03-30 06:30:47\n" +
                "5,Thacher,Okenfold,76,tokenfold4@purevolume.com,Bigender,172.53.50.228,\"Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4\",2021-12-05 18:53:14").build();

        this.assert0(easyLoader, properties);
    }
    @Test
    public void loadJson() {
        final LoaderProperties properties = LoaderProperties.builder().url("/easydata/loader/website_user.json").build();

        this.assert0(EasyLoader.FILE_LOADER, properties);
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