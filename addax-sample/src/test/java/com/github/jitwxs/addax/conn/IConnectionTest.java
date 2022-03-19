package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.common.enums.DataTypeEnum;
import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.provider.LoaderPropertiesProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import com.github.jitwxs.addax.sample.properties.OrderEvaluateProperties;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 0:04
 */
public class IConnectionTest {
    @Test
    public void loadingCsvFromSpi() {
        final LoaderPropertiesProvider provider = ProviderFactory.delegate(LoaderPropertiesProvider.class);

        final LoaderProperties properties = provider.delegate(OrderEvaluate.class);

        assertNotNull(properties);

        assert0(new FileConnection(), properties);
    }

    @Test
    public void loadingCsvFromParams() {
        assert0(new FileConnection(), new OrderEvaluateProperties());
    }

    @Test
    public void loading() {
        final MySQLConnection connection = new MySQLConnection(
                "com.mysql.cj.jdbc.Driver",
                "root",
                "root",
                "jdbc:mysql://localhost:3306/express");

        assert0(connection, new OrderEvaluateProperties());
    }

    public static void assert0(final IConnection connection, final LoaderProperties properties) {
        final Optional<Pair<DataTypeEnum, List<String[]>>> optional = connection.loading(properties);

        assertTrue(optional.isPresent());

        final Pair<DataTypeEnum, List<String[]>> pair = optional.get();

        assertEquals(DataTypeEnum.MATRIX, pair.getLeft());
        assertEquals(2, pair.getRight().size());
    }
}
