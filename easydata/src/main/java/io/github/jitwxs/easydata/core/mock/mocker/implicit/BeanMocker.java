package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import io.github.jitwxs.easydata.common.annotation.EasyMockIgnore;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.ClassGroupEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.function.ThrowableBiFunction;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;

/**
 * java bean
 */
@AllArgsConstructor
public class BeanMocker implements IMocker<Object> {

    private final Class<?> target;

    @Override
    public Object mock(MockConfig mockConfig) {
        try {
            final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc = (name, type) -> {
                // contrastClass 支持
                final Class<?> contrastClass = mockConfig.getContrastClass();
                if (contrastClass != null) {
                    final FieldProperty property = PropertyCache.tryGet(contrastClass, name);

                    if (property != null) {
                        final Object value = new BaseMocker<>(property.getType()).mock(mockConfig);
                        return ProviderFactory.delegate(ConvertProvider.class).convert(value, (Class<?>) type);
                    }
                }

                return new BaseMocker<>(type).mock(mockConfig);
            };

            switch (ClassGroupEnum.delegate(target)) {
                case PROTOBUF_MESSAGE:
                    return ObjectUtils.createProtoMessage(target, fieldGeneratorFunc);
                case PROTOBUF_BUILDER:
                    return ObjectUtils.createProtoBuilder(target, fieldGeneratorFunc);
                case NATIVE:
                    return ObjectUtils.createJava(target, field -> field.isAnnotationPresent(EasyMockIgnore.class), fieldGeneratorFunc);
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (Throwable e) {
            throw new EasyDataMockException(e);
        }
    }
}
