package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import com.google.protobuf.Message;
import io.github.jitwxs.easydata.common.annotation.EasyMockIgnore;
import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.function.ThrowableBiFunction;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
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
            final ThrowableBiFunction<String, Type, Object> fieldGeneratorFunc = (name, type) -> new BaseMocker<>(type).mock(mockConfig);

            if (Message.class.isAssignableFrom(target)) {
                return ObjectUtils.createProto(target, fieldGeneratorFunc);
            } else {
                return ObjectUtils.createJava(target, field -> field.isAnnotationPresent(EasyMockIgnore.class), fieldGeneratorFunc);
            }
        } catch (Throwable e) {
            throw new EasyDataMockException(e);
        }
    }
}
