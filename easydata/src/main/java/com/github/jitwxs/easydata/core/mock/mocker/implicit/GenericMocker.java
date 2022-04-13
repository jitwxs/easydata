package com.github.jitwxs.easydata.core.mock.mocker.implicit;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import com.github.jitwxs.easydata.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;

import java.lang.reflect.ParameterizedType;

/**
 * 模拟泛型
 */
@AllArgsConstructor
public class GenericMocker implements IMocker<Object> {

    private final ParameterizedType type;

    @Override
    public Object mock(MockConfig mockConfig) {
        return new BaseMocker(type.getRawType(), type.getActualTypeArguments()).mock(mockConfig);
    }
}