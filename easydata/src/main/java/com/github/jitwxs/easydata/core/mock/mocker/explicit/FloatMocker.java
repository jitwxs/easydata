package com.github.jitwxs.easydata.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * Float对象模拟器
 */
public class FloatMocker implements IMocker<Float> {

    @Override
    public Float mock(MockConfig mockConfig) {
        return RandomUtils.nextFloat(mockConfig.getFloatRange()[0], mockConfig.getFloatRange()[1]);
    }

}
