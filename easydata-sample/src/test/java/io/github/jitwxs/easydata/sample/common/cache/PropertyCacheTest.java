package io.github.jitwxs.easydata.sample.common.cache;

import io.github.jitwxs.easydata.common.bean.ClassProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jitwxs
 */
public class PropertyCacheTest {
    @Test
    public void testGetProtobufWithRepeatField() {
        final ClassProperty property = PropertyCache.tryGet(MessageProto.OrderEvaluateManage.class);

        assertEquals(2, property.getAll().size());
        assertEquals(2, property.getReadable().size());

        final MessageProto.OrderEvaluateManage evaluateManage = EasyMock.run(MessageProto.OrderEvaluateManage.class);

        assertNotNull(evaluateManage);
        assertNotEquals(0, evaluateManage.getOrderEvaluateCount());
    }
}