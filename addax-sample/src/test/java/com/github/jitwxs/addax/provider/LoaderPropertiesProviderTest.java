package com.github.jitwxs.addax.provider;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoaderPropertiesProviderTest {
    @Test
    public void loadingBySpi() {
        final LoaderPropertiesProvider provider = ProviderFactory.delegate(LoaderPropertiesProvider.class);

        final LoaderProperties properties = provider.delegate(OrderEvaluate.class);

        assertNotNull(properties);
    }
}