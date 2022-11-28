package io.github.jitwxs.easydata.core.verify.comp;

import org.assertj.core.util.FloatComparator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloatComparatorTest {
    @Test
    public void testNegativeComp1() {
        final float a = -53.1966F, b = -53.1968F;

        // a > b
        assertEquals(1, new FloatComparator(0F).compare(a, b));

        // a == b
        assertEquals(0, new FloatComparator(1E-3F).compare(a, b));

        // a > b
        assertEquals(1, new FloatComparator(1E-5F).compare(a, b));
    }

    @Test
    public void testNegativeComp2() {
        final float b = -53.1966F, a = -53.1968F;

        // a < b
        assertEquals(-1, new FloatComparator(0F).compare(a, b));

        // a == b
        assertEquals(0, new FloatComparator(1E-3F).compare(a, b));

        // a < b
        assertEquals(-1, new FloatComparator(1E-5F).compare(a, b));
    }

    @Test
    public void testPositiveComp1() {
        final float a = 53.1966F, b = 53.1968F;

        // a < b
        assertEquals(-1, new FloatComparator(0F).compare(a, b));

        // a == b
        assertEquals(0, new FloatComparator(1E-3F).compare(a, b));

        // a < b
        assertEquals(-1, new FloatComparator(1E-5F).compare(a, b));
    }

    @Test
    public void testPositiveComp2() {
        final float b = 53.1966F, a = 53.1968F;

        // a > b
        assertEquals(1, new FloatComparator(0F).compare(a, b));

        // a == b
        assertEquals(0, new FloatComparator(1E-3F).compare(a, b));

        // a > b
        assertEquals(1, new FloatComparator(1E-5F).compare(a, b));
    }
}