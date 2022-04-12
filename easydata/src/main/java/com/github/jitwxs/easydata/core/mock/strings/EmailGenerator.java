package com.github.jitwxs.easydata.core.mock.strings;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.common.enums.MockStringEnum;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 邮箱生成器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:05
 */
public class EmailGenerator implements IStringGenerator {
    @Override
    public String generator(MockConfig mockConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append(RandomStringUtils.randomAlphanumeric(10));
        sb.append("@");
        sb.append(RandomStringUtils.randomAlphanumeric(5));
        sb.append(".");
        sb.append(RandomStringUtils.randomAlphanumeric(3));

        return sb.toString().toLowerCase();
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.EMAIL;
    }
}
