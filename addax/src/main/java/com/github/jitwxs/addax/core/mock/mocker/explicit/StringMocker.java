package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import com.github.jitwxs.addax.provider.ProviderFactory;
import com.github.jitwxs.addax.provider.StringGeneratorProvider;

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
