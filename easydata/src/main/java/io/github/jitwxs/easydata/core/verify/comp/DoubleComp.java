package io.github.jitwxs.easydata.core.verify.comp;

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
    public int compare0(Double o1, Double o2) {
        final double diff = o1 - o2;

        final int compareTo = Double.compare(diff, 0D);
        if (compareTo == 0) {
            return 0;
        }

        final double remaining = Math.abs(diff) - this.precisionConfig;

        if (Double.compare(remaining, 0D) <= 0) {
            return 0;
        }

        return compareTo;
    }
}
