package io.github.jitwxs.easydata.sample.core.mock.primitive;

import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.core.mock.TypeKit;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * test primitive data struct support with easymock
 *
 * this case will use <a href="https://github.com/vigna/fastutil">fastutil</a> library
 *
 * @author jitwxs@foxmail.com
 * @since 2023-04-11 0:15
 */
public class FastutilPrimitiveStructTest {
    @Test
    @DisplayName("fastutil | key map")
    public void testFastutilaKMap() {
        // initial with TypeKit
        final Long2ObjectOpenHashMap<String> map = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<Long2ObjectOpenHashMap<String>>() {
        }));
        assertFalse(map.isEmpty());

        // initial with generic
        final Long2ObjectOpenHashMap map1 = assertDoesNotThrow(() -> EasyMock.run(Long2ObjectOpenHashMap.class));
        assertFalse(map1.isEmpty());
    }

    @Test
    @DisplayName("fastutil | value map")
    public void testFastutilaVMap() {
        // initial with TypeKit
        final Object2IntOpenHashMap<String> map = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<Object2IntOpenHashMap<String>>() {
        }));
        assertFalse(map.isEmpty());

        // initial with generic
        final Object2IntOpenHashMap map1 = assertDoesNotThrow(() -> EasyMock.run(Object2IntOpenHashMap.class));
        assertFalse(map1.isEmpty());
    }

    @Test
    @DisplayName("fastutil | key-value map")
    public void testFastutilaKVMap() {
        // initial with TypeKit
        final Long2LongOpenHashMap map = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<Long2LongOpenHashMap>() {
        }));
        assertFalse(map.isEmpty());

        // initial with generic
        final Long2LongOpenHashMap map1 = assertDoesNotThrow(() -> EasyMock.run(Long2LongOpenHashMap.class));
        assertFalse(map1.isEmpty());
    }

    @Test
    @DisplayName("fastutil | key set")
    public void testFastutilaKSet() {
        // initial with TypeKit
        final LongOpenHashSet set = assertDoesNotThrow(() -> EasyMock.run(new TypeKit<LongOpenHashSet>() {
        }));
        assertFalse(set.isEmpty());

        // initial with generic
        final LongOpenHashSet set1 = assertDoesNotThrow(() -> EasyMock.run(LongOpenHashSet.class));
        assertFalse(set1.isEmpty());
    }
}
