package io.github.jitwxs.easydata.core.mock.strings;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.enums.MockStringEnum;

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
