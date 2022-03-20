package com.github.jitwxs.addax.core.mock.mocker.implicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.BaseMocker;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 模拟Collection
 */
@AllArgsConstructor
public class CollectionMocker implements IMocker<Object> {

    private final Class<?> clazz;

    private final Type genericType;

    @Override
    public Object mock(MockConfig mockConfig) {
        int size = mockConfig.nexSize();
        Collection<Object> result;
        if (List.class.isAssignableFrom(clazz)) {
            result = new ArrayList<>(size);
        } else {
            result = new HashSet<>(size);
        }
        BaseMocker baseMocker = new BaseMocker(genericType);
        for (int index = 0; index < size; index++) {
            result.add(baseMocker.mock(mockConfig));
        }
        return result;
    }

}
