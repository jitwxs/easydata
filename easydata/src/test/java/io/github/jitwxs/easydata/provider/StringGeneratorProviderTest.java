package io.github.jitwxs.easydata.provider;

import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import io.github.jitwxs.easydata.core.mock.strings.IStringGenerator;
import io.github.jitwxs.easydata.core.mock.strings.CNIdCardGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringGeneratorProviderTest {
    @Test
    public void testDelegate() {
        final StringGeneratorProvider provider = ProviderFactory.delegate(StringGeneratorProvider.class);

        final IStringGenerator generator = provider.delegate(MockStringEnum.CN_ID_CARD);

        assertNotNull(generator);
        assertEquals(CNIdCardGenerator.class, generator.getClass());
    }
}