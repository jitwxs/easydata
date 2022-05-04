package io.github.jitwxs.easydata.core.mock.strings;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.enums.MockStringEnum;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:04
 */
public interface IStringGenerator {
    String generator(MockConfig mockConfig);

    MockStringEnum type();
}
