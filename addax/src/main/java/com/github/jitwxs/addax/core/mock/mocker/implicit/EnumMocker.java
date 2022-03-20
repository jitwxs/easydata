package com.github.jitwxs.addax.core.mock.mocker.implicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.cache.EnumCache;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

@AllArgsConstructor
public class EnumMocker<T extends Enum<?>> implements IMocker<Object> {

    private final Class<?> clazz;

    @Override
    public T mock(MockConfig mockConfig) {
        final Enum<?>[] enums = EnumCache.tryGet(clazz);
        if (enums == null || enums.length == 0) {
            return null;
        }

        return (T) enums[RandomUtils.nextInt(0, enums.length)];
    }
}
