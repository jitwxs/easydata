package com.github.jitwxs.easydata.sample.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.core.mock.mocker.IMocker;
import com.github.jitwxs.easydata.sample.provider.ProviderFactory;
import com.github.jitwxs.easydata.sample.provider.StringGeneratorProvider;

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
