package io.github.jitwxs.easydata.core.verify.comp;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 21:25
 */
public class FloatComp extends BaseComp<Float> {

    protected FloatComp() {
        super(0F);
    }

    protected FloatComp(Float precisionConfig) {
        super(precisionConfig);
    }

    @Override
    public int compare0(Float o1, Float o2) {
        final float diff = o1 - o2;

        final int compareTo = Float.compare(diff, 0F);
        if (compareTo == 0) {
            return 0;
        }

        final float remaining = Math.abs(diff) - this.precisionConfig;

        if (Float.compare(remaining, 0F) <= 0) {
            return 0;
        }

        return compareTo;
    }
}
