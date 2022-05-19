package io.github.jitwxs.easydata.core.verify.comp;

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
    public int compare0(Long o1, Long o2) {
        final long diff = o1 - o2;

        final int compareTo = Long.compare(diff, 0L);
        if (compareTo == 0) {
            return 0;
        }

        final long remaining = Math.abs(diff) - this.precisionConfig;

        if (Long.compare(remaining, 0L) <= 0) {
            return 0;
        }

        return compareTo;
    }
}
