package io.github.jitwxs.easydata.core.verify.impl;

import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.verify.VerifyInstance;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.github.jitwxs.easydata.core.verify.EasyVerify.BEAN_TYPE_VERIFY;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:37
 */
public class MapTypeVerify {
    public void check(Map a, Map b, VerifyInstance instance) {
        final Class<?> targetClass = this.fetchTargetClass(a, b);

        final Set<Object> ignoreElements = instance.getIgnoreElements();

        a = this.alignmentClass(targetClass, a, ignoreElements);
        b = this.alignmentClass(targetClass, b, ignoreElements);

        // 不继续向下消费
        instance = instance.clone();
        instance.getIgnoreElements().clear();

        // 比较 keySet 是否相等
        BEAN_TYPE_VERIFY.check(a.keySet(), b.keySet(), instance);

        // 比较对应 value 是否相等
        for (Object key : a.keySet()) {
            BEAN_TYPE_VERIFY.check(a.get(key), b.get(key), instance);
        }
    }

    private Class<?> fetchTargetClass(final Map<?, ?> a, final Map<?, ?> b) {
        if (a.getClass() == b.getClass()) {
            return a.getClass();
        }

        return HashMap.class;
    }

    /**
     * 统一 class 类型
     *
     * @param target         要转换的目标类型
     * @param oldBean        原始对象
     * @param ignoreElements 忽略的元素
     * @return 转换后的类型
     */
    private Map alignmentClass(Class<?> target, Map<?, ?> oldBean, Set<Object> ignoreElements) {
        if (CollectionUtils.isEmpty(ignoreElements) && target == oldBean.getClass()) {
            return oldBean;
        }

        final Map newBean = (Map) ObjectUtils.create(target, null);

        oldBean.forEach((k, v) -> {
            boolean isIgnore = false;

            for (Object ignoreElement : ignoreElements) {
                if (ignoreElement.equals(k)) {
                    isIgnore = true;
                    break;
                }
            }

            if (!isIgnore) {
                newBean.put(k, v);
            }
        });

        return newBean;
    }
}
