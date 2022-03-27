package com.github.jitwxs.addax.core.verify.comp;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 21:25
 */
public class LongComp extends BaseComp<Long> {

    protected LongComp() {
        super(0L);
    }

    protected LongComp(Long precisionConfig) {
        super(precisionConfig);
    }

    @Override
    public int compare(Long o1, Long o2) {
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
