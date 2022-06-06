package io.github.jitwxs.easydata.sample.core.mock;

import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import io.github.jitwxs.easydata.sample.bean.UniqueUser;
import io.github.jitwxs.easydata.sample.enums.SexEnum;
import io.github.jitwxs.easydata.sample.message.EnumProto;
import io.github.jitwxs.easydata.sample.message.MessageProto;
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

        final MessageProto.OrderEvaluate.Builder orderEvaluate3 = EasyMock.run(MessageProto.OrderEvaluate.Builder.class);
        assertNotNull(orderEvaluate3);
    }

    @Test
    public void testPropertyDescriptor() {
        final UniqueUser user = EasyMock.run(UniqueUser.class);

        assertNotNull(user);
    }

    @Test
    public void testMockProtoOneof() {
        final MessageProto.OrderEvaluateOperation operation = EasyMock.run(MessageProto.OrderEvaluateOperation.class);

        assertNotNull(operation);
    }
}
