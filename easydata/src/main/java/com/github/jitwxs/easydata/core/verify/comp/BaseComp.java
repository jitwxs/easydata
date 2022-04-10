package com.github.jitwxs.easydata.core.verify.comp;

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
}
