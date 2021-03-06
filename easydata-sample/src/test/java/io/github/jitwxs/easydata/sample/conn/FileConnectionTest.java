package io.github.jitwxs.easydata.sample.conn;

import io.github.jitwxs.easydata.conn.FileConnection;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
import org.junit.jupiter.api.Test;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 0:04
 */
public class FileConnectionTest extends BaseConnectionTest {
    @Test
    public void fileConnection() {
        assert0(new FileConnection(), LoaderProperties.builder().url("/easydata/loader/order_evaluate.csv").build());
    }
}
