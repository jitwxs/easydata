package io.github.jitwxs.easydata.core.verify.comp;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalCompTest {

    @Test
    public void testNegativeComp1() {
        final BigDecimal a = new BigDecimal("-53.5612005108291966"), b = new BigDecimal("-53.5612005108291968");

        // a > b
        assertEquals(1, new BigDecimalComp().compare(a, b));

        // a == b
        assertEquals(0, new BigDecimalComp(BigDecimal.valueOf(1E-15)).compare(a, b));

        // a > b
        assertEquals(1, new BigDecimalComp(BigDecimal.valueOf(1E-17)).compare(a, b));
    }

    @Test
    public void testNegativeComp2() {
        final BigDecimal b = new BigDecimal("-53.5612005108291966"), a = new BigDecimal("-53.5612005108291968");

        // a < b
        assertEquals(-1, new BigDecimalComp().compare(a, b));

        // a == b
        assertEquals(0, new BigDecimalComp(BigDecimal.valueOf(1E-15)).compare(a, b));

        // a < b
        assertEquals(-1, new BigDecimalComp(BigDecimal.valueOf(1E-17)).compare(a, b));
    }

    @Test
    public void testPositiveComp1() {
        final BigDecimal a = new BigDecimal("53.5612005108291966"), b = new BigDecimal("53.5612005108291968");

        // a < b
        assertEquals(-1, new BigDecimalComp().compare(a, b));

        // a == b
        assertEquals(0, new BigDecimalComp(BigDecimal.valueOf(1E-15)).compare(a, b));

        // a < b
        assertEquals(-1, new BigDecimalComp(BigDecimal.valueOf(1E-17)).compare(a, b));
    }

    @Test
    public void testPositiveComp2() {
        final BigDecimal b = new BigDecimal("53.5612005108291966"), a = new BigDecimal("53.5612005108291968");

        // a > b
        assertEquals(1, new BigDecimalComp().compare(a, b));

        // a == b
        assertEquals(0, new BigDecimalComp(BigDecimal.valueOf(1E-15)).compare(a, b));

        // a > b
        assertEquals(1, new BigDecimalComp(BigDecimal.valueOf(1E-17)).compare(a, b));
    }
}