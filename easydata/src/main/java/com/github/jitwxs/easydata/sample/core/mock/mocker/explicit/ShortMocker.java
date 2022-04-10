package com.github.jitwxs.easydata.sample.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * 模拟Short对象
 */
public class ShortMocker implements IMocker<Short> {

    @Override
    public Short mock(MockConfig mockConfig) {
        return (short) RandomUtils.nextInt(mockConfig.getShortRange()[0], mockConfig.getShortRange()[1]);
    }

}
