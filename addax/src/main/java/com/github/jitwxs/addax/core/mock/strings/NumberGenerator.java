package com.github.jitwxs.addax.core.mock.strings;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.enums.MockStringEnum;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 身份证号生成器
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
