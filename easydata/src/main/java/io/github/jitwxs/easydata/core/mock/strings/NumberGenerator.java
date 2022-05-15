package io.github.jitwxs.easydata.core.mock.strings;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import org.apache.commons.lang3.RandomUtils;

/**
 * 数字生成器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:05
 */
public class NumberGenerator implements IStringGenerator {

    @Override
    public String generator(MockConfig mockConfig) {
        final int count = mockConfig.nexSize();

        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; i++) {
            sb.append(RandomUtils.nextInt(i == 0 ? 1 : 0, 10));
        }

        return sb.toString();
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.NUMBER;
    }

}
