package com.github.jitwxs.addax.core.mock;

import com.github.jitwxs.addax.sample.enums.SexEnum;
import com.github.jitwxs.addax.sample.message.EnumProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 17:10
 */
public class MockTest {
    @Test
    public void testEnumMock() {
        final SexEnum mock1 = Mock.run(SexEnum.class);
        assertNotNull(mock1);

        final EnumProto.SexEnum mock2 = Mock.run(EnumProto.SexEnum.class);
        assertNotNull(mock2);
    }
}
