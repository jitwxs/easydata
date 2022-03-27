package com.github.jitwxs.addax.core.verify.comp;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 21:25
 */
public class IntegerComp extends BaseComp<Integer> {

    protected IntegerComp() {
        super(0);
    }

    protected IntegerComp(Integer precisionConfig) {
        super(precisionConfig);
    }

    @Override
    public int compare(Integer o1, Integer o2) {
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
