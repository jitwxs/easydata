package com.github.jitwxs.easydata.sample.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * Double对象模拟器
 */
public class DoubleMocker implements IMocker<Double> {

    @Override
    public Double mock(MockConfig mockConfig) {
        return RandomUtils.nextDouble(mockConfig.getFloatRange()[0], mockConfig.getFloatRange()[1]);
    }

}
