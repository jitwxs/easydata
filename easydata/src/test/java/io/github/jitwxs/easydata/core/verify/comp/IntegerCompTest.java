package io.github.jitwxs.easydata.core.verify.comp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerCompTest {
    @Test
    public void testNegativeComp1() {
        final int a = -53, b = -55;

        // a > b
        assertEquals(1, new IntegerComp().compare(a, b));

        // a == b
        assertEquals(0, new IntegerComp(2).compare(a, b));

        // a > b
        assertEquals(1, new IntegerComp(1).compare(a, b));
    }

    @Test
    public void testNegativeComp2() {
        final int b = -53, a = -55;

        // a < b
        assertEquals(-1, new IntegerComp().compare(a, b));

        // a == b
        assertEquals(0, new IntegerComp(2).compare(a, b));

        // a < b
        assertEquals(-1, new IntegerComp(1).compare(a, b));
    }

    @Test
    public void testPositiveComp1() {
        final int a = 53, b = 55;

        // a < b
        assertEquals(-1, new IntegerComp().compare(a, b));

        // a == b
        assertEquals(0, new IntegerComp(2).compare(a, b));

        // a < b
        assertEquals(-1, new IntegerComp(1).compare(a, b));
    }

    @Test
    public void testPositiveComp2() {
        final int b = 53, a = 55;

        // a > b
        assertEquals(1, new IntegerComp().compare(a, b));

        // a == b
        assertEquals(0, new IntegerComp(2).compare(a, b));

        // a > b
        assertEquals(1, new IntegerComp(1).compare(a, b));
    }
}