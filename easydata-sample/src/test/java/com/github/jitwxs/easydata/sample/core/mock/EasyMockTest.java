package com.github.jitwxs.easydata.sample.core.mock;

import com.github.jitwxs.easydata.core.mock.EasyMock;
import com.github.jitwxs.easydata.sample.sample.bean.OrderEvaluate;
import com.github.jitwxs.easydata.sample.sample.enums.SexEnum;
import com.github.jitwxs.easydata.sample.sample.message.EnumProto;
import com.github.jitwxs.easydata.sample.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 17:10
 */
public class EasyMockTest {
    @Test
    public void testEnumMock() {
        final SexEnum mock1 = EasyMock.run(SexEnum.class);
        assertNotNull(mock1);

        final EnumProto.SexEnum mock2 = EasyMock.run(EnumProto.SexEnum.class);
        assertNotNull(mock2);
    }

    @Test
    public void testBeanMock() {
        final OrderEvaluate orderEvaluate = EasyMock.run(OrderEvaluate.class);
        assertNotNull(orderEvaluate);

        final MessageProto.OrderEvaluate orderEvaluate2 = EasyMock.run(MessageProto.OrderEvaluate.class);
        assertNotNull(orderEvaluate2);
    }
}
