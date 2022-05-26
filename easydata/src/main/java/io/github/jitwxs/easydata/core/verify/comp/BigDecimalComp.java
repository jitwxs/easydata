package io.github.jitwxs.easydata.core.verify.comp;

import io.github.jitwxs.easydata.common.util.BigDecimalUtils;

import java.math.BigDecimal;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 21:25
 */
public class BigDecimalComp extends BaseComp<BigDecimal> {

    protected BigDecimalComp() {
        super(BigDecimal.ZERO);
    }

    protected BigDecimalComp(BigDecimal precisionConfig) {
        super(BigDecimalUtils.zeroIfNull(precisionConfig));
    }

    @Override
    public int compare0(BigDecimal o1, BigDecimal o2) {
        final BigDecimal diff = o1.subtract(o2);

        final int compareTo = diff.compareTo(BigDecimal.ZERO);
        if (compareTo == 0) {
            return 0;
        }

        final BigDecimal remaining = diff.abs().subtract(this.precisionConfig);

        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        return compareTo;
    }
}
