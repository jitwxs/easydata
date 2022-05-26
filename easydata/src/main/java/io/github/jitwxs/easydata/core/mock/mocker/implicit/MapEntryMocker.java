package io.github.jitwxs.easydata.core.mock.mocker.implicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.util.CollectionUtils;
import io.github.jitwxs.easydata.core.mock.mocker.BaseMocker;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-26 21:55
 */
public class MapEntryMocker implements IMocker<Object> {
    private final Class<?> target;

    private final Type[] types;

    public MapEntryMocker(Class<?> target, Type[] paramTypes) {
        this.target = target;

        this.types = new Type[2];

        int i = 0;
        for (; i < paramTypes.length; i++) {
            this.types[i] = paramTypes[i];
        }

        while (i < this.types.length) {
            this.types[i++] = String.class;
        }
    }

    @Override
    public Object mock(MockConfig mockConfig) {
        final Map<Object, Object> map = new HashMap<Object, Object>() {{
            put(new BaseMocker(types[0]).mock(mockConfig), new BaseMocker(types[1]).mock(mockConfig));
        }};

        final Map.Entry<Object, Object> result = CollectionUtils.randomElement(map.entrySet());

        final ConvertProvider provider = ProviderFactory.delegate(ConvertProvider.class);

        return provider.convert(result, target);
    }
}
