package com.github.jitwxs.easydata.core.verify.comp;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 21:25
 */
public class DoubleComp extends BaseComp<Double> {

    protected DoubleComp() {
        super(0D);
    }

    protected DoubleComp(Double precisionConfig) {
        super(precisionConfig);
    }

    @Override
    public int compare(Double o1, Double o2) {
        final int i = o1.compareTo(o2);

        if (i == 0) {
            return i;
        }

        if (i < 0) {
            o1 = o1 + precisionConfig;
        } else {
            o2 = o2 + precisionConfig;
        }

        return o1.compareTo(o2);
    }
}
