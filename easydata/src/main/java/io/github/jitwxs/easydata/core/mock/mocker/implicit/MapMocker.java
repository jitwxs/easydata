package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.common.util.ReflectionUtils;
import io.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * 模拟Map
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@AllArgsConstructor
public class MapMocker implements IMocker<Object> {

    private final Class<?> clazz;

    private final Type[] types;

    @Override
    public Object mock(MockConfig mockConfig) {
        final Type[] kvType = getKVType();
        final BaseMocker keyMocker = new BaseMocker(kvType[0]);
        final BaseMocker valueMocker = new BaseMocker(kvType[1]);

        final Map result = (Map) ObjectUtils.create(clazz, mockConfig.getConstructorSupplier(clazz));
        for (int index = 0; index < mockConfig.nexSize(); index++) {
            result.put(keyMocker.mock(mockConfig), valueMocker.mock(mockConfig));
        }
        return result;
    }

    private Type[] getKVType() {
        if (types.length == 2) {
            return new Type[] {types[0], types[1]};
        } else {
            final Type[] res = getGenericTypes();
            for (int i = 0, j = 0; i < res.length; i++) {
                if (res[i] instanceof TypeVariable) {
                    // use object to fault
                    res[i] = j >= types.length ?  Object.class : types[j++];
                }
            }
            return res;
        }
    }

    private Type[] getGenericTypes() {
        final List<Class<?>> classList = new ArrayList<>();
        classList.add(this.clazz);
        ReflectionUtils.loadSuperAndInterfaceClass(this.clazz, classList);

        for (Class<?> loopClass : classList) {
            if (!Map.class.isAssignableFrom(loopClass)) {
                continue;
            }

            Type[] ones = ReflectionUtils.getGenericSuperClass(loopClass);
            if (ones != null && ones.length == 2) {
                return ones;
            }

            for (Type anInterface : loopClass.getGenericInterfaces()) {
                if (anInterface instanceof ParameterizedType) {
                    ones = ((ParameterizedType) anInterface).getActualTypeArguments();
                    if (ones != null && ones.length == 2) {
                        return ones;
                    }
                }
            }
        }

        throw new EasyDataMockException("MapMocker getGenericTypes failed, class: " + this.clazz);
    }
}
