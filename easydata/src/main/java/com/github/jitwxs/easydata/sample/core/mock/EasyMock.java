package com.github.jitwxs.easydata.sample.core.mock;

import com.github.jitwxs.easydata.sample.EasyDataEntrance;
import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.core.mock.mocker.BaseMocker;

/**
 * data mock module entrance
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:44
 */
public class EasyMock extends EasyDataEntrance {
    /**
     * mock simple class instance, using default mockConfig
     *
     * @param target mock target simple class
     * @param <T>    mock target class generic
     * @return mock result instance
     */
    public static <T> T run(Class<T> target) {
        return run(target, new MockConfig());
    }

    /**
     * mock simple class instance, specify mockConfig
     *
     * @param target     mock target simple class
     * @param <T>        mock target class generic
     * @param mockConfig mock configuration
     * @return mock result instance
     */
    public static <T> T run(Class<T> target, MockConfig mockConfig) {
        return new BaseMocker<T>(target).mock(mockConfig);
    }

    /**
     * mock complex class instance, using default mockConfig
     *
     * @param reference mock target complex class
     * @param <T>       mock target class generic
     * @return mock result instance
     */
    public static <T> T run(TypeKit<T> reference) {
        return run(reference, new MockConfig());
    }

    /**
     * mock complex class instance, specify mockConfig
     *
     * @param reference  mock target complex class
     * @param mockConfig mock configuration
     * @param <T>        mock target class generic
     * @return mock result instance
     */
    public static <T> T run(TypeKit<T> reference, MockConfig mockConfig) {
        return new BaseMocker<T>(reference.getType()).mock(mockConfig.init(reference.getType()));
    }
}
