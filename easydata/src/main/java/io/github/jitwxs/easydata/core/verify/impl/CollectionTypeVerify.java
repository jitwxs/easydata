package io.github.jitwxs.easydata.core.verify.impl;

import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.verify.VerifyInstance;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static io.github.jitwxs.easydata.core.verify.EasyVerify.BEAN_TYPE_VERIFY;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:37
 */
public class CollectionTypeVerify {
    public void check(Collection a, Collection b, VerifyInstance instance) {
        final Class<?> targetClass = this.fetchTargetClass(a, b);

        final Set<Object> ignoreElements = instance.getIgnoreElements();

        a = alignmentClass(targetClass, a, ignoreElements);
        b = alignmentClass(targetClass, b, ignoreElements);

        BEAN_TYPE_VERIFY.doOneEquals(a, targetClass, b, targetClass, instance);
    }

    private Class<?> fetchTargetClass(final Collection<?> a, final Collection<?> b) {
        if (a.getClass() == b.getClass()) {
            return a.getClass();
        }

        final boolean usingSet = Stream.of(a.getClass(), b.getClass()).anyMatch(Set.class::isAssignableFrom);

        return usingSet ? HashSet.class : ArrayList.class;
    }

    /**
     * 统一类型
     *
     * @param target         要转换的目标类型
     * @param oldBean        原始对象
     * @param ignoreElements 忽略的元素
     * @return 转换后的类型
     */
    private Collection alignmentClass(Class<?> target, Collection<?> oldBean, Set<Object> ignoreElements) {
        if (CollectionUtils.isEmpty(ignoreElements) && target == oldBean.getClass()) {
            return oldBean;
        }

        final Collection newBean = (Collection) ObjectUtils.create(target);

        for (Object item : oldBean) {
            boolean isIgnore = false;

            for (Object ignoreElement : ignoreElements) {
                if (ignoreElement.equals(item)) {
                    isIgnore = true;
                    break;
                }
            }

            if (!isIgnore) {
                newBean.add(item);
            }
        }

        return newBean;
    }
}
