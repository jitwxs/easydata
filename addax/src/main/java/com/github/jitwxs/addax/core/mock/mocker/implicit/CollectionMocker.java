package com.github.jitwxs.addax.core.mock.mocker.implicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.exception.AddaxMockException;
import com.github.jitwxs.addax.common.util.ObjectUtils;
import com.github.jitwxs.addax.core.mock.mocker.BaseMocker;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;

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
        if (result == null) {
            throw new AddaxMockException("Not Support CollectionMocker For: " + clazz);
        }

        final BaseMocker<?> baseMocker = new BaseMocker<>(genericType);

        for (int i = 0; i < mockConfig.nexSize(); i++) {
            result.add(baseMocker.mock(mockConfig));
        }

        return result;
    }

}
