package com.github.jitwxs.easydata.sample.core.verify.impl;

import com.github.jitwxs.easydata.sample.common.util.ObjectUtils;
import com.github.jitwxs.easydata.sample.core.verify.VerifyInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.github.jitwxs.easydata.sample.core.verify.EasyVerify.BEAN_TYPE_VERIFY;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:37
 */
public class CollectionTypeVerify {
    public void check(Collection a, Collection b, VerifyInstance instance) {
        // 数量比较
        assertThat(a).hasSameSizeAs(b);

        // 类型统一
        if (a.getClass() != b.getClass()) {
            // 如果存在 set，就使用 set 进行统一
            final boolean usingSet = Stream.of(a.getClass(), b.getClass()).anyMatch(Set.class::isAssignableFrom);

            final Class<?> clazz = usingSet ? HashSet.class : ArrayList.class;

            a = alignmentClass(clazz, a);
            b = alignmentClass(clazz, b);
        }

        BEAN_TYPE_VERIFY.doOneEquals(a, b, instance);
    }

    private Collection alignmentClass(Class<?> target, Collection value) {
        final Collection newValue = (Collection) ObjectUtils.create(target);

        newValue.addAll(value);

        return newValue;
    }
}
