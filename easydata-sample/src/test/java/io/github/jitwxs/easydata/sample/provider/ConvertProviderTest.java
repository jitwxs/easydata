package io.github.jitwxs.easydata.sample.provider;

import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import org.junit.jupiter.api.Test;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:31
 */
public class ConvertProviderTest {
    @Test
    public void convertDb2Proto() {
        final ConvertProvider convertProvider = ProviderFactory.delegate(ConvertProvider.class);

    }
}