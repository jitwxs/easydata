package io.github.jitwxs.easydata.core.verify.comp;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Comparator;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 21:26
 */
public abstract class BaseComp<T> implements Comparator<T> {
    protected final T precisionConfig;

    protected BaseComp(T precisionConfig) {
        this.precisionConfig = precisionConfig;
    }

    protected abstract int compare0(T o1, T o2);

    @Override
    public int compare(T o1, T o2) {
        // 非 null 场景
        if (ObjectUtils.allNotNull(o1, o2)) {
            return compare0(o1, o2);
        }

        final boolean a = o1 == null, b = o2 == null;

        if (a && b) {
            return 0;
        } else if (a) {
            return -1;
        } else {
            return 1;
        }
    }
}
