package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
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
