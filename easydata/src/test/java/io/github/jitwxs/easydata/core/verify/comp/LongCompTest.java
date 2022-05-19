package io.github.jitwxs.easydata.core.verify.comp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LongCompTest {
    @Test
    public void testNegativeComp1() {
        final long a = -53, b = -55;

        // a > b
        assertEquals(1, new LongComp().compare(a, b));

        // a == b
        assertEquals(0, new LongComp(2L).compare(a, b));

        // a > b
        assertEquals(1, new LongComp(1L).compare(a, b));
    }

    @Test
    public void testNegativeComp2() {
        final long b = -53, a = -55;

        // a < b
        assertEquals(-1, new LongComp().compare(a, b));

        // a == b
        assertEquals(0, new LongComp(2L).compare(a, b));

        // a < b
        assertEquals(-1, new LongComp(1L).compare(a, b));
    }

    @Test
    public void testPositiveComp1() {
        final long a = 53, b = 55;

        // a < b
        assertEquals(-1, new LongComp().compare(a, b));

        // a == b
        assertEquals(0, new LongComp(2L).compare(a, b));

        // a < b
        assertEquals(-1, new LongComp(1L).compare(a, b));
    }

    @Test
    public void testPositiveComp2() {
        final long b = 53, a = 55;

        // a > b
        assertEquals(1, new LongComp().compare(a, b));

        // a == b
        assertEquals(0, new LongComp(2L).compare(a, b));

        // a > b
        assertEquals(1, new LongComp(1L).compare(a, b));
    }
}