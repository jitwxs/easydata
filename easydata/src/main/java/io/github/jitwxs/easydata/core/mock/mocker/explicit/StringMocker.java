package io.github.jitwxs.easydata.core.mock.mocker.explicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import io.github.jitwxs.easydata.provider.StringGeneratorProvider;

/**
 * 模拟String对象
 */
public class StringMocker implements IMocker<String> {
    @Override
    public String mock(MockConfig mockConfig) {
        final StringGeneratorProvider provider = ProviderFactory.delegate(StringGeneratorProvider.class);
        return provider.delegate(mockConfig.getStringEnum()).generator(mockConfig);
    }
}
