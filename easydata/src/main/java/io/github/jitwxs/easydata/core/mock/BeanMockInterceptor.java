package io.github.jitwxs.easydata.core.mock;

import io.github.jitwxs.easydata.common.bean.MockConfig;

/**
 * bean mock 拦截器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-07-23 16:59
 */
public interface BeanMockInterceptor<T> {
    /**
     * 单个字段的 mock 行为
     *
     * @param fieldName  字段名
     * @param mockConfig mockConfig
     * @return mock 结果
     */
    Object mock(String fieldName, MockConfig mockConfig);

    enum MockRes {
        /**
         * 调用 mock
         */
        CALL_MOCK,

        /**
         * 跳过 mock
         */
        SKIP_MOCK
    }
}
