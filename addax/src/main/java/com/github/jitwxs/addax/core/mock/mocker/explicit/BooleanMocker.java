package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * Boolean对象模拟器
 */
public class BooleanMocker implements IMocker<Boolean> {

    @Override
    public Boolean mock(MockConfig mockConfig) {
        return RandomUtils.nextBoolean();
    }

}
