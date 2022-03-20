package com.github.jitwxs.addax.core.mock.mocker.implicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.BaseMocker;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟Map
 */
@AllArgsConstructor
public class MapMocker implements IMocker<Object> {

    private final Type[] types;

    @Override
    public Object mock(MockConfig mockConfig) {
        int size = mockConfig.nexSize();
        Map<Object, Object> result = new HashMap<>(size);
        BaseMocker keyMocker = new BaseMocker(types[0]);
        BaseMocker valueMocker = new BaseMocker(types[1]);
        for (int index = 0; index < size; index++) {
            result.put(keyMocker.mock(mockConfig), valueMocker.mock(mockConfig));
        }
        return result;
    }
}
