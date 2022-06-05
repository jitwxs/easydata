package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.bean.ObjectImpl;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import io.github.jitwxs.easydata.provider.ExplicitMockerProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class ClassMocker implements IMocker<Object> {
    private final Class<?> clazz;

    private final Type[] genericTypes;

    private final ExplicitMockerProvider explicitProvider = ProviderFactory.delegate(ExplicitMockerProvider.class);

    public ClassMocker(Class<?> clazz, Type[] genericTypes) {
        this.clazz = clazz;
        this.genericTypes = genericTypes;
    }

    @Override
    public Object mock(MockConfig mockConfig) {
        // fault process
        if (clazz == Class.class) {
            return Object.class;
        }
        if (clazz == Object.class) {
            return ObjectUtils.create(ObjectImpl.class);
        }

        IMocker<?> mocker;
        if (clazz.isArray()) {
            mocker = new ArrayMocker(clazz);
        } else if (Map.class.isAssignableFrom(clazz)) {
            mocker = new MapMocker(genericTypes);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            mocker = new CollectionMocker(clazz, genericTypes[0]);
        } else if (clazz.isEnum()) {
            mocker = new EnumMocker<>(clazz);
        } else if (Map.Entry.class.isAssignableFrom(clazz)) {
            mocker = new MapEntryMocker(clazz, genericTypes);
        } else {
            mocker = explicitProvider.delegate(clazz);
            if (mocker == null) {
                mocker = new BeanMocker(clazz);
            }
        }
        return mocker.mock(mockConfig);
    }
}
