package io.github.jitwxs.easydata.sample.core.verify;

import io.github.jitwxs.easydata.core.verify.EasyVerify;
import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 精度误差验证
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 11:18
 */
public class PrecisionVerifyTest {
    @Test
    public void testString() {
        assertThrows(AssertionError.class, () -> EasyVerify.with("xxx", "yyy").verify());
    }

    @Test
    @DisplayName("Integer 类型")
    public void testInteger() {
        // 支持精度误差，包装类型
        final Integer a = 1, b = 2;
        assertDoesNotThrow(() -> EasyVerify.with(a, b).withPrecision(1).verify());
        assertThrows(AssertionError.class, () -> EasyVerify.with(a, b).withPrecision(0.9).verify());

        // 支持精度误差，基本类型
        final int c = 3, d = 5;
        EasyVerify.with(c, d).withPrecision(2).verify();
    }

    @Test
    @DisplayName("Double 类型")
    public void testDouble() {
        EasyVerify.with(1D, 1.0D).verify();

        // 支持精度误差，包装类型
        final Double a = 1D, b = 1.1D;
        EasyVerify.with(a, b).withPrecision(0.2D).verify();

        // 支持精度误差，基本类型
        final double c = 1D, d = 1.1D;
        EasyVerify.with(c, d).withPrecision(0.2D).verify();

        // 精度误差配置类型错误
        assertThrows(AssertionError.class, () -> EasyVerify.with(c, d).withPrecision(0.1F).verify());
    }

    @Test
    @DisplayName("BigDecimal 类型")
    public void testBigDecimalPrecision() {
        // 默认使用 compare 比较，而不是 equals
        EasyVerify.with(valueOf(1), valueOf(1.0)).verify();
        EasyVerify.with(valueOf(1.00000), valueOf(1.0)).verify();

        // 支持精度误差
        assertDoesNotThrow(() -> EasyVerify.with(valueOf(1), valueOf(1.1)).withPrecision(valueOf(0.1)).verify());
        EasyVerify.with(valueOf(1.2), valueOf(1.1)).withPrecision(valueOf(0.1)).verify();

        // 精度误差配置类型错误
        assertThrows(AssertionError.class, () -> EasyVerify.with(valueOf(1), 1.1).withPrecision(valueOf(0.1)).verify());
    }

    @Test
    @DisplayName("对象中的 BigDecimal 类型")
    public void testBigDecimalPrecision2() {
        // 默认使用 compare 比较，而不是 equals
        final OrderEvaluate evaluate1 = OrderEvaluate.builder().userScore(valueOf(1.0)).build();
        final OrderEvaluate evaluate2 = OrderEvaluate.builder().userScore(valueOf(1.00)).build();
        EasyVerify.with(evaluate1, evaluate2).validateFields("userScore").verify();

        // 支持精度误差
        final OrderEvaluate evaluate3 = OrderEvaluate.builder().userScore(valueOf(1.0)).build();
        final OrderEvaluate evaluate4 = OrderEvaluate.builder().userScore(valueOf(1.001)).build();
        EasyVerify.with(evaluate3, evaluate4).validateFields("userScore").withPrecision(valueOf(0.001)).verify();
    }
}
