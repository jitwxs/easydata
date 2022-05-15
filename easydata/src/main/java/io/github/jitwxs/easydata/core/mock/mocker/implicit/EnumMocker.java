package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.cache.EnumCache;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class EnumMocker<T extends Enum<?>> implements IMocker<Object> {

    private final Class<?> clazz;

    @Override
    public T mock(MockConfig mockConfig) {
        final Collection<Enum> enums = EnumCache.tryGet(clazz).getNameMap().values();

        return (T) enums.iterator().next();
    }
}
