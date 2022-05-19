package io.github.jitwxs.easydata.core.verify.comp;

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
    public int compare0(Integer o1, Integer o2) {
        final int diff = o1 - o2;

        final int compareTo = Integer.compare(diff, 0);
        if (compareTo == 0) {
            return 0;
        }

        final int remaining = Math.abs(diff) - this.precisionConfig;

        if (remaining <= 0) {
            return 0;
        }

        return compareTo;
    }
}
