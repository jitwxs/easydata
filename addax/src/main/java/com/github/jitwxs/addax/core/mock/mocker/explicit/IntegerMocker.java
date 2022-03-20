package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * Integer对象模拟器
 */
public class IntegerMocker implements IMocker<Integer> {

    @Override
    public Integer mock(MockConfig mockConfig) {
        return RandomUtils.nextInt(mockConfig.getIntRange()[0], mockConfig.getIntRange()[1]);
    }

}
