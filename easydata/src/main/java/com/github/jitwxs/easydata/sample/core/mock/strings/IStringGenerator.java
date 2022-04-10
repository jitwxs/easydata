package com.github.jitwxs.easydata.sample.core.mock.strings;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.common.enums.MockStringEnum;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:04
 */
public interface IStringGenerator {
    String generator(MockConfig mockConfig);

    MockStringEnum type();
}
