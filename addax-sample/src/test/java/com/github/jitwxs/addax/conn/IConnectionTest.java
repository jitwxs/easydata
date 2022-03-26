package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.common.bean.MatrixBean;
import com.github.jitwxs.addax.common.enums.DataTypeEnum;
import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.core.loader.LoadingSource;
import com.github.jitwxs.addax.provider.LoaderPropertiesProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 0:04
 */
public class IConnectionTest {
    @Test
    public void fileConnection() {
        final Class<OrderEvaluate> target = OrderEvaluate.class;

        final LoaderProperties properties = LoaderProperties.builder()
                .clazz(target)
                .fileUrl("/addax/loader/order_evaluate.csv")
                .build();

        LoaderPropertiesProvider.register(properties);

        assertEquals(properties, ProviderFactory.delegate(LoaderPropertiesProvider.class).delegate(target));

        assert0(new FileConnection(), properties);
    }

//
//    @Test
//    public void loading() {
//        final MySQLConnection connection = new MySQLConnection(
//                "com.mysql.cj.jdbc.Driver",
//                "root",
//                "root",
//                "jdbc:mysql://localhost:3306/express");
//
//        assert0(connection, new OrderEvaluateProperties());
//    }

    public static void assert0(final IConnection connection, final LoaderProperties properties) {
        final Optional<LoadingSource<?>> optional = connection.loading(properties);

        assertTrue(optional.isPresent());

        final LoadingSource<?> loadingSource = optional.get();

        assertEquals(DataTypeEnum.MATRIX, loadingSource.getDataType());
        assertTrue(loadingSource.getSource() instanceof MatrixBean);

        final MatrixBean source = (MatrixBean) loadingSource.getSource();
        assertEquals(1, source.getDataList().size());
    }
}
