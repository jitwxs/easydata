package com.github.jitwxs.easydata.core.mock;

import com.github.jitwxs.easydata.sample.enums.SexEnum;
import com.github.jitwxs.easydata.sample.message.EnumProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 17:10
 */
public class MockTest {
    @Test
    public void testEnumMock() {
        final SexEnum mock1 = EasyMock.run(SexEnum.class);
        assertNotNull(mock1);

        final EnumProto.SexEnum mock2 = EasyMock.run(EnumProto.SexEnum.class);
        assertNotNull(mock2);
    }
}
