package com.github.jitwxs.easydata.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * Byte对象模拟器
 */
public class ByteMocker implements IMocker<Byte> {

    @Override
    public Byte mock(MockConfig mockConfig) {
        return (byte) RandomUtils.nextInt(mockConfig.getByteRange()[0], mockConfig.getByteRange()[1]);
    }

}
