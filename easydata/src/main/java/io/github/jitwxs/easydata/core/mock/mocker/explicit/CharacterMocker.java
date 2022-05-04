package io.github.jitwxs.easydata.core.mock.mocker.explicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Character对象模拟器
 */
public class CharacterMocker implements IMocker<Character> {

    @Override
    public Character mock(MockConfig mockConfig) {
        final String s = RandomStringUtils.randomAlphanumeric(1);
        return s.toCharArray()[0];
    }
}
