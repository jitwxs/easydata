package com.github.jitwxs.addax.core.verify.impl;

import com.github.jitwxs.addax.common.util.ObjectUtils;
import com.github.jitwxs.addax.core.verify.VerifyContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.github.jitwxs.addax.core.verify.Verify.BEAN_TYPE_VERIFY;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:37
 */
public class CollectionTypeVerify {
    public void check(Collection a, Collection b, VerifyContext context) {
        // 数量比较
        assertThat(a).hasSameSizeAs(b);

        // 类型统一
        if (a.getClass() != b.getClass()) {
            // 如果有一个类型为 set，就使用 set 进行统一
            final boolean usingSet = Stream.of(a.getClass(), b.getClass()).anyMatch(Set.class::isAssignableFrom);

            final Class<?> clazz = usingSet ? HashSet.class : ArrayList.class;

            a = alignmentClass(clazz, a);
            b = alignmentClass(clazz, b);
        }

        BEAN_TYPE_VERIFY.doOneEquals(a, b, context);
    }

    public Collection alignmentClass(Class<?> target, Collection value) {
        final Collection newValue = (Collection) ObjectUtils.create(target);

        newValue.addAll(value);

        return newValue;
    }
}
