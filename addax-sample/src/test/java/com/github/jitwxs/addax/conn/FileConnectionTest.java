package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.provider.LoaderPropertiesProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 0:04
 */
public class FileConnectionTest extends BaseConnectionTest {
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
}
