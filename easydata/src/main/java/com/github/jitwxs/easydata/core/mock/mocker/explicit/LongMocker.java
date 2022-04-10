package com.github.jitwxs.easydata.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * 模拟Long对象
 */
public class LongMocker implements IMocker<Long> {

    @Override
    public Long mock(MockConfig mockConfig) {
        return RandomUtils.nextLong(mockConfig.getLongRange()[0], mockConfig.getLongRange()[1]);
    }

}
