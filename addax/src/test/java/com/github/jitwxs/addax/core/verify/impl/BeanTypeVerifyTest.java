package com.github.jitwxs.addax.core.verify.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
}