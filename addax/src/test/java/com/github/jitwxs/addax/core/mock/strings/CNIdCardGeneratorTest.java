package com.github.jitwxs.addax.core.mock.strings;

import com.github.jitwxs.addax.common.bean.MockConfig;
import org.junit.jupiter.api.Test;

public class CNIdCardGeneratorTest {
    private final CNIdCardGenerator generator = new CNIdCardGenerator();

    @Test
    public void generator() {
        final MockConfig mockConfig = new MockConfig();
        mockConfig.setDateRange(new String[]{"1990-01-01 08:00:00.000", "2022-03-20 23:59:59.999"});

        final String strings = this.generator.generator(mockConfig);

        System.out.println(strings);
    }
}