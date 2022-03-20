package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.enums.MockStringEnum;
import com.github.jitwxs.addax.common.exception.AddaxMockException;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

/**
 * 模拟String对象
 */
public class StringMocker implements IMocker<String> {
    @Override
    public String mock(MockConfig mockConfig) {
        final MockStringEnum stringEnum = mockConfig.getStringEnum();

        switch (stringEnum) {
            case CHARACTER:
                return mockerCharacter(mockConfig);
            case UUID:
                return UUID.randomUUID().toString();
            case NUMBER:
                return RandomStringUtils.randomNumeric(mockConfig.nexSize());
            default:
                throw new AddaxMockException("Not Support MockStringEnum");
        }
    }

    /**
     * 生成随机多个字符
     */
    private String mockerCharacter(MockConfig mockConfig) {
        int size = mockConfig.nexSize();
        String[] stringSeed = mockConfig.getStringSeed();
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(stringSeed[RandomUtils.nextInt(0, stringSeed.length)]);
        }
        return sb.toString();
    }
}
