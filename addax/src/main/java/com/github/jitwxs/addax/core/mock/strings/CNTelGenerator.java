package com.github.jitwxs.addax.core.mock.strings;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.enums.MockStringEnum;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.RandomUtils.nextInt;

/**
 * 中国手机号码生成器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:05
 */
public class CNTelGenerator implements IStringGenerator {
    private static final int[] MOBILE_PREFIX = new int[]{133, 153, 177, 180,
            181, 189, 134, 135, 136, 137, 138, 139, 150, 151, 152, 157, 158, 159,
            178, 182, 183, 184, 187, 188, 130, 131, 132, 155, 156, 176, 185, 186,
            145, 147, 170};

    @Override
    public String generator(MockConfig mockConfig) {
        return MOBILE_PREFIX[nextInt(0, MOBILE_PREFIX.length)] + StringUtils.leftPad("" + nextInt(0, 99999999 + 1), 8, "0");
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.CN_TEL;
    }
}
