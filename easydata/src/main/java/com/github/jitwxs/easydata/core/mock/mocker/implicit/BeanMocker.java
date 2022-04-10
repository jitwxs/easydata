package com.github.jitwxs.easydata.core.mock.mocker.implicit;

import com.github.jitwxs.easydata.common.annotation.EasyMockIgnore;
import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.common.cache.PropertyCache;
import com.github.jitwxs.easydata.common.exception.EasyDataMockException;
import com.github.jitwxs.easydata.common.util.ObjectUtils;
import com.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import com.github.jitwxs.easydata.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@AllArgsConstructor
public class BeanMocker implements IMocker<Object> {

    private final Class<?> clazz;

    @Override
    public Object mock(MockConfig mockConfig) {
        try {
            final Object result = ObjectUtils.create(clazz);

            for (Map.Entry<String, PropertyDescriptor> entry : PropertyCache.tryGet(clazz).getWriteAble().entrySet()) {
                final Field field = PropertyCache.tryGetField(clazz, entry.getKey());
                if (field.isAnnotationPresent(EasyMockIgnore.class)) {
                    continue;
                }

                final Object fieldValue = new BaseMocker<>(field.getGenericType()).mock(mockConfig);

                entry.getValue().getWriteMethod().invoke(result, fieldValue);
            }

            return result;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new EasyDataMockException(e);
        }
    }
}
