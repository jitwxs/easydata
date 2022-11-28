package io.github.jitwxs.easydata.core.verify.impl;

import io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BeanTypeVerifyTest {
    @InjectMocks
    private BeanTypeVerify verify;

    private static Stream<Class<?>> notRecursiveScenario() {
        return Stream.of(
                int.class, // 基本类型
                Short.class, // 包装类型类型
                BigInteger.class, BigDecimal.class,
                String.class, Date.class,
                LocalDate.class, LocalDateTime.class
        );
    }

    @ParameterizedTest
    @MethodSource("notRecursiveScenario")
    public void isEnableRecursiveCompare(final Class<?> target) {
        assertFalse(verify.isEnableRecursiveCompare(target));
    }

    @Test
    public void checkForDifferentClass() {
        assertThrows(AssertionError.class, () -> EasyVerify.with(20480L, 20480).verify());
        assertDoesNotThrow(() -> EasyVerify.with(20480L, 20480).ignoreClassDiff(ClassDiffVerifyStrategyEnum.CONVERT_SAME_CLASS).verify());
    }
}