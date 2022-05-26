package io.github.jitwxs.easydata.core.mock;

import io.github.jitwxs.easydata.common.bean.IgnoreBean;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-26 21:26
 */
public class SpecialClassMockTest {
    @Test
    public void testMockPair() {
        final Pair<String, String> stringPair = EasyMock.run(new TypeKit<Pair<String, String>>() {
        });
        assertNotNull(stringPair);

        final Pair objectPair = EasyMock.run(Pair.class);
        assertNotNull(objectPair);

        final Pair<Integer, IgnoreBean> beanPair = EasyMock.run(new TypeKit<Pair<Integer, IgnoreBean>>() {
        });
        assertNotNull(beanPair);
    }
}
