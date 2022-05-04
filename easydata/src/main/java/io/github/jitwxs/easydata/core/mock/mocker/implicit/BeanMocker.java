package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import io.github.jitwxs.easydata.common.annotation.EasyMockIgnore;
import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import com.google.protobuf.Message;
import lombok.AllArgsConstructor;
import org.powermock.reflect.Whitebox;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * java bean
 */
@AllArgsConstructor
public class BeanMocker implements IMocker<Object> {

    private final Class<?> clazz;

    @Override
    public Object mock(MockConfig mockConfig) {
        try {
            return Message.class.isAssignableFrom(clazz) ? mockProtoBean(mockConfig) : mockJavaBean(mockConfig);
        } catch (Exception e) {
            throw new EasyDataMockException(e);
        }
    }

    private Object mockJavaBean(MockConfig mockConfig) throws Exception {
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
    }

    private Object mockProtoBean(MockConfig mockConfig) throws Exception {
        final Object invoke = ObjectUtils.createProtoBuilder(clazz);
        if (invoke == null) {
            return null;
        }

        for (Map.Entry<String, PropertyDescriptor> entry : PropertyCache.tryGet(clazz).getReadable().entrySet()) {
            final String field = entry.getKey();
            final PropertyDescriptor descriptor = entry.getValue();

            final Class<?> paramType = descriptor.getPropertyType();
            final String writeMethodName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);

            final Object fieldValue = new BaseMocker<>(paramType).mock(mockConfig);

            final Method method = Whitebox.getMethod(invoke.getClass(), writeMethodName, paramType);

            method.invoke(invoke, fieldValue);
        }

        return ObjectUtils.buildProtoBuilder(invoke);
    }
}
