package io.github.jitwxs.easydata.core.mock.strings;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import org.apache.commons.lang3.RandomStringUtils;
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

        String numeric = RandomStringUtils.randomNumeric(count);

        // 多位数避免生成首位为0
        if (count > 1 && numeric.startsWith("0")) {
            numeric = numeric.substring(1) + RandomUtils.nextInt(1, 10);
        }

        return numeric;
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.NUMBER;
    }

}
