package com.github.jitwxs.easydata.sample.core.mock.strings;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.common.enums.MockStringEnum;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 身份证号生成器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:05
 */
public class EnglishGenerator implements IStringGenerator {

    @Override
    public String generator(MockConfig mockConfig) {
        return RandomStringUtils.randomAlphabetic(mockConfig.nexSize());
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.ENGLISH;
    }

}
