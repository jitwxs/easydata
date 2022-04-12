package com.github.jitwxs.easydata.core.mock.mocker;

import com.github.jitwxs.easydata.common.bean.MockConfig;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:47
 */
public interface IMocker<T> {
    /**
     * 模拟数据
     *
     * @param mockConfig 模拟数据配置
     * @return 模拟数据对象
     */
    T mock(MockConfig mockConfig);
}
