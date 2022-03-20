package com.github.jitwxs.addax.core.mock;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.BaseMocker;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:44
 */
public class Mock {
    /**
     * 模拟数据
     *
     * @param clazz 模拟数据类型
     * @return 模拟数据对象
     */
    public static <T> T run(Class<T> clazz) {
        return run(clazz, new MockConfig());
    }

    /**
     * 模拟数据
     *
     * @param clazz 模拟数据类型
     * @param mockConfig 模拟数据配置
     * @return 模拟数据对象
     */
    public static <T> T run(Class<T> clazz, MockConfig mockConfig) {
        return new BaseMocker<T>(clazz).mock(mockConfig);
    }

    /**
     * 模拟数据
     * <pre>
     * 注意typeReference必须以{}结尾
     * </pre>
     *
     * @param typeReference 模拟数据包装类型
     * @return 模拟数据对象
     */
    public static <T> T run(TypeKit<T> typeReference) {
        return run(typeReference, new MockConfig());
    }

    /**
     * 模拟数据
     * <pre>
     * 注意typeReference必须以{}结尾
     * </pre>
     *
     * @param typeReference 模拟数据类型
     * @param mockConfig 模拟数据配置
     * @return 模拟数据对象
     */
    public static <T> T run(TypeKit<T> typeReference, MockConfig mockConfig) {
        return new BaseMocker<T>(typeReference.getType()).mock(mockConfig.init(typeReference.getType()));
    }
}
