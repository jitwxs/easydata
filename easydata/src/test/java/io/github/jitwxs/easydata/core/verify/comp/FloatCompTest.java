package io.github.jitwxs.easydata.core.verify.comp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FloatCompTest {
    @Test
    public void testNegativeComp1() {
        final float a = -53.1966F, b = -53.1968F;

        // a > b
        assertEquals(1, new FloatComp().compare(a, b));

        // a == b
        assertEquals(0, new FloatComp(1E-3F).compare(a, b));

        // a > b
        assertEquals(1, new FloatComp(1E-5F).compare(a, b));
    }

    @Test
    public void testNegativeComp2() {
        final float b = -53.1966F, a = -53.1968F;

        // a < b
        assertEquals(-1, new FloatComp().compare(a, b));

        // a == b
        assertEquals(0, new FloatComp(1E-3F).compare(a, b));

        // a < b
        assertEquals(-1, new FloatComp(1E-5F).compare(a, b));
    }

    @Test
    public void testPositiveComp1() {
        final float a = 53.1966F, b = 53.1968F;

        // a < b
        assertEquals(-1, new FloatComp().compare(a, b));

        // a == b
        assertEquals(0, new FloatComp(1E-3F).compare(a, b));

        // a < b
        assertEquals(-1, new FloatComp(1E-5F).compare(a, b));
    }

    @Test
    public void testPositiveComp2() {
        final float b = 53.1966F, a = 53.1968F;

        // a > b
        assertEquals(1, new FloatComp().compare(a, b));

        // a == b
        assertEquals(0, new FloatComp(1E-3F).compare(a, b));

        // a > b
        assertEquals(1, new FloatComp(1E-5F).compare(a, b));
    }
}