package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

/**
 * Character对象模拟器
 */
public class CharacterMocker implements IMocker<Character> {

    @Override
    public Character mock(MockConfig mockConfig) {
        char[] charSeed = mockConfig.getCharSeed();
        return charSeed[RandomUtils.nextInt(0, charSeed.length)];
    }
}
