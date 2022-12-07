package io.github.jitwxs.easydata.common.util;

import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jitwxs
 * @see ReflectionUtils
 */
public class ReflectionUtilsTest {

    @Test
    public void testType() {
        final Method method = Whitebox.getMethod(ReflectionUtilsTest.class, "testGenericMethod");

        final Type returnType = method.getGenericReturnType();

        assertNotNull(returnType);
    }

    public List<Integer> testGenericMethod() {
        return null;
    }
}