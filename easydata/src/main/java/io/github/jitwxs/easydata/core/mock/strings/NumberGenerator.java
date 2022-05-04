package io.github.jitwxs.easydata.core.mock.strings;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 数字生成器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:05
 */
public class NumberGenerator implements IStringGenerator {

    @Override
    public String generator(MockConfig mockConfig) {
        return RandomStringUtils.randomNumeric(mockConfig.nexSize());
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.NUMBER;
    }

}
