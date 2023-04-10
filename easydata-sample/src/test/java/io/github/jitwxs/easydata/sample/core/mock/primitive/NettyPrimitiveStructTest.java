package io.github.jitwxs.easydata.sample.core.mock.primitive;

import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.core.mock.TypeKit;
import io.netty.util.collection.LongObjectHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * test primitive data struct support with easymock
 *
 * this case will use <a href="https://github.com/netty/netty">netty</a> library
 *
 * @author jitwxs@foxmail.com
 * @since 2023-04-11 0:15
 */
public class NettyPrimitiveStructTest {
    @Test
    @DisplayName("netty | key map")
    public void testNettyKMap() {
        // initial with TypeKit
        final LongObjectHashMap<String> map = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<LongObjectHashMap<String>>() {
        }));
        assertFalse(map.isEmpty());

        // initial with generic
        final LongObjectHashMap map1 = assertDoesNotThrow(() -> EasyMock.run(LongObjectHashMap.class));
        assertFalse(map1.isEmpty());
    }
}
