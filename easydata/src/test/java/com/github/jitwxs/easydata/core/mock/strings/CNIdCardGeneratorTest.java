package com.github.jitwxs.easydata.core.mock.strings;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import org.junit.jupiter.api.Test;

public class CNIdCardGeneratorTest {
    private final CNIdCardGenerator generator = new CNIdCardGenerator();

    @Test
    public void testGenerator() {
        final MockConfig mockConfig = new MockConfig();
        mockConfig.setDateRange(0L, 10001L);

        final String strings = this.generator.generator(mockConfig);

        System.out.println(strings);
    }
}