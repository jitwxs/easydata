package io.github.jitwxs.easydata.core.mock.strings;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        try (final MockedStatic<RandomStringUtils> mockStatic = mockStatic(RandomStringUtils.class)) {
            mockStatic.when(() -> RandomStringUtils.randomNumeric(anyInt())).thenReturn("012");

            final String strings = this.generator.generator(new MockConfig());

            assertEquals(3, strings.length());
            assertTrue(strings.startsWith("12"));
        }
    }
}