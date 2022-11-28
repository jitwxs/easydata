package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;

import java.lang.reflect.Type;
import java.util.*;

/**
 * 模拟Collection
 */
public class CollectionMocker implements IMocker<Object> {

    private final Class<?> clazz;

    private final Type genericType;

    private static final Map<Class<?>, Class<?>> interfaceDefaultImplMap = new HashMap<Class<?>, Class<?>>() {{
        put(List.class, ArrayList.class);
        put(Set.class, HashSet.class);
    }};

    public CollectionMocker(Class<?> clazz, Type genericType) {
        this.clazz = clazz;
        this.genericType = genericType;
    }

    @Override
    public Object mock(MockConfig mockConfig) {
        final Class<?> actualClass = interfaceDefaultImplMap.getOrDefault(clazz, clazz);

        final Collection<Object> result = (Collection<Object>) ObjectUtils.create(actualClass);

        final BaseMocker<?> baseMocker = new BaseMocker<>(genericType);

        for (int i = 0; i < mockConfig.nexSize(); i++) {
            result.add(baseMocker.mock(mockConfig));
        }

        return result;
    }

}
