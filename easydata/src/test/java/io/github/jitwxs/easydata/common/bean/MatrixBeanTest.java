package io.github.jitwxs.easydata.common.bean;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixBeanTest {
    @Test
    public void testNewInstanceWithNull() {
        final MatrixBean matrixBean = MatrixBean.newInstance(null);

        assertEquals(matrixBean, MatrixBean.FALLBACK_BEAN);
        assertTrue(MatrixBean.ignore(matrixBean));
    }

    @Test
    public void testNewInstance() {
        final MatrixBean matrixBean = MatrixBean.newInstance(Arrays.asList(new String[0], new String[0]));

        assertNotEquals(matrixBean, MatrixBean.FALLBACK_BEAN);
        assertFalse(MatrixBean.ignore(matrixBean));
    }
}