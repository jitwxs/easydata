package io.github.jitwxs.easydata.core.mock.strings;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumberGeneratorTest {
    private final IStringGenerator generator = new NumberGenerator();

    @Test
    public void testGenerator() {
        final MockConfig mockConfig = new MockConfig();

        // 0 ~ 99
        mockConfig.setSizeRange(1, 2);

        final String strings = this.generator.generator(mockConfig);

        final int number = Integer.parseInt(strings);

        assertTrue(number >= 0 && number < 100);
    }

    @Test
    public void testGeneratorWithFirstZero() {
        final String strings = this.generator.generator(new MockConfig());

        assertFalse(strings.startsWith("0"));
    }
}