package com.github.jitwxs.addax.common.util;

import java.math.BigDecimal;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 21:31
 */
public class BigDecimalUtil {
    public static BigDecimal zeroIfNull(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }

    public static boolean equalZero(BigDecimal val) {
        if (val == null) {
            return false;
        }
        return val.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean lessEqualZero(BigDecimal val) {
        if (val == null) {
            return false;
        }
        return val.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean lessThen(BigDecimal arg1, BigDecimal arg2) {
        return arg1.compareTo(arg2) < 0;
    }

    public static boolean equal(BigDecimal arg1, BigDecimal arg2) {
        return arg1.compareTo(arg2) == 0;
    }
}
