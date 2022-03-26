package com.github.jitwxs.addax.core.verify.impl;

import com.github.jitwxs.addax.common.util.ObjectUtils;
import com.github.jitwxs.addax.core.verify.VerifyContext;

import java.util.HashMap;
import java.util.Map;

import static com.github.jitwxs.addax.core.verify.Verify.BEAN_TYPE_VERIFY;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:37
 */
public class MapTypeVerify {
    public void check(Map a, Map b, VerifyContext context) {
        // 数量比较
        assertThat(a).hasSameSizeAs(b);

        // 类型统一
        if (a.getClass() != b.getClass()) {
            a = alignmentClass(HashMap.class, a);
            b = alignmentClass(HashMap.class, b);
        }

        // 比较 keySet 是否相等
        BEAN_TYPE_VERIFY.check(a.keySet(), b.keySet(), context);

        // 比较对应 value 是否相等
        for (Object key : a.keySet()) {
            BEAN_TYPE_VERIFY.check(a.get(key), b.get(key), context);
        }
    }

    public Map alignmentClass(Class<?> target, Map value) {
        final Map newValue = (Map) ObjectUtils.create(target);

        newValue.putAll(value);

        return newValue;
    }
}
