package com.github.jitwxs.easydata.sample.core.mock.strings;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.common.enums.MockStringEnum;

import java.util.UUID;

import static org.apache.commons.lang3.RandomUtils.nextInt;

/**
 * 身份证号生成器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:05
 */
public class UuidGenerator implements IStringGenerator {

    @Override
    public String generator(MockConfig mockConfig) {
        return UUID.randomUUID().toString();
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.UUID;
    }
}