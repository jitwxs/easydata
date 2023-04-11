package io.github.jitwxs.easydata.sample.core.mock.primitive;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.core.mock.TypeKit;
import org.agrona.collections.Long2LongHashMap;
import org.agrona.collections.Long2ObjectHashMap;
import org.agrona.collections.LongHashSet;
import org.agrona.collections.Object2IntHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test primitive data struct support with easymock
 *
 * this case will use <a href="https://github.com/real-logic/agrona">agrona</a> library
 *
 * @author jitwxs@foxmail.com
 * @since 2023-04-11 0:15
 */
public class AgronaPrimitiveStructTest {
    @Test
    @DisplayName("agrona | key map")
    public void testAgronaKMap() {
        // initial with TypeKit
        final Long2ObjectHashMap<String> map = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<Long2ObjectHashMap<String>>() {
        }));
        assertFalse(map.isEmpty());

        // initial with generic
        final Long2ObjectHashMap map1 = assertDoesNotThrow(() -> EasyMock.run(Long2ObjectHashMap.class));
        assertFalse(map1.isEmpty());
    }

    @Test
    @DisplayName("agrona | value map")
    public void testAgronaVMap() {
        final Supplier<MockConfig> mockConfigSupplier = () -> new MockConfig().registerConstructorSupplier(Object2IntHashMap.class, () -> new Object2IntHashMap(-1));

        // initial with TypeKit
        final Object2IntHashMap<String> map = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<Object2IntHashMap<String>>() {
        }, mockConfigSupplier.get()));
        assertFalse(map.isEmpty());

        // initial with generic
        final Object2IntHashMap map1 = assertDoesNotThrow(() -> EasyMock.run(Object2IntHashMap.class, mockConfigSupplier.get()));
        assertFalse(map1.isEmpty());
    }

    @Test
    @DisplayName("agrona | key-value map")
    public void testAgronaKVMap() {
        final Supplier<MockConfig> mockConfigSupplier = () -> new MockConfig().registerConstructorSupplier(Long2LongHashMap.class, () -> new Long2LongHashMap(-1));

        // initial with TypeKit
        final Long2LongHashMap map = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<Long2LongHashMap>() {
        }, mockConfigSupplier.get()));
        assertFalse(map.isEmpty());

        // initial with generic
        final Long2LongHashMap map1 = assertDoesNotThrow(() -> EasyMock.run(Long2LongHashMap.class, mockConfigSupplier.get()));
        assertFalse(map1.isEmpty());
    }

    @Test
    @DisplayName("agrona | key set")
    public void testAgronaKSet() {
        // initial with TypeKit
        final LongHashSet set = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<LongHashSet>() {
        }));
        assertFalse(set.isEmpty());

        // initial with generic
        final LongHashSet set1 = assertDoesNotThrow(() -> EasyMock.run(LongHashSet.class));
        assertFalse(set1.isEmpty());
    }
}
