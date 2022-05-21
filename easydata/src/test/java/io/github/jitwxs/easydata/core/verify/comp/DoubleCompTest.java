package io.github.jitwxs.easydata.core.verify.comp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoubleCompTest {
    @Test
    public void testNegativeComp1() {
        final double a = -53.1966, b = -53.1968;

        // a > b
        assertEquals(1, new DoubleComp().compare(a, b));

        // a == b
        assertEquals(0, new DoubleComp(1E-3).compare(a, b));

        // a > b
        assertEquals(1, new DoubleComp(1E-5).compare(a, b));
    }

    @Test
    public void testNegativeComp2() {
        final double b = -53.1966, a = -53.1968;

        // a < b
        assertEquals(-1, new DoubleComp().compare(a, b));

        // a == b
        assertEquals(0, new DoubleComp(1E-3).compare(a, b));

        // a < b
        assertEquals(-1, new DoubleComp(1E-5).compare(a, b));
    }

    @Test
    public void testPositiveComp1() {
        final double a = 53.1966, b = 53.1968;

        // a < b
        assertEquals(-1, new DoubleComp().compare(a, b));

        // a == b
        assertEquals(0, new DoubleComp(1E-3).compare(a, b));

        // a < b
        assertEquals(-1, new DoubleComp(1E-5).compare(a, b));
    }

    @Test
    public void testPositiveComp2() {
        final double b = 53.1966, a = 53.1968;

        // a > b
        assertEquals(1, new DoubleComp().compare(a, b));

        // a == b
        assertEquals(0, new DoubleComp(1E-3).compare(a, b));

        // a > b
        assertEquals(1, new DoubleComp(1E-5).compare(a, b));
    }
}