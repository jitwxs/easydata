package io.github.jitwxs.easydata.core.verify.comp;

import io.github.jitwxs.easydata.common.util.BigDecimalUtil;

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
        super(BigDecimalUtil.zeroIfNull(precisionConfig));
    }

    @Override
    public int compare0(BigDecimal o1, BigDecimal o2) {
        final int i = o1.compareTo(o2);

        if (i == 0) {
            return i;
        }

        if (i < 0) {
            o1 = o1.add(precisionConfig);
        } else {
            o2 = o2.add(precisionConfig);
        }

        return o1.compareTo(o2);
    }
}
