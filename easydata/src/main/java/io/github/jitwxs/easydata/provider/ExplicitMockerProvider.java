package io.github.jitwxs.easydata.provider;

import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.common.util.ReflectionUtils;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveWrapper;
import static org.apache.commons.lang3.ClassUtils.wrapperToPrimitive;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 13:22
 */
public class ExplicitMockerProvider extends Provider<IMocker, Class<?>> {
    @Override
    protected List<IMocker> loadNative() {
        final List<IMocker> resultList = new ArrayList<>();

        final String scanPackage = IMocker.class.getPackage().getName() + ".explicit";

        new Reflections(scanPackage).getSubTypesOf(IMocker.class).forEach(c -> {
            if (Modifier.isInterface(c.getModifiers())) {
                return;
            }
            resultList.add(ObjectUtils.create(c));
        });

        return resultList;
    }

    @Override
    protected List<Object> uniqueKeyByInstance(IMocker instance) {
        final Class<? extends IMocker> clazz = instance.getClass();

        final Type[] arguments = ReflectionUtils.getGenericInterface0Class(clazz);

        if (arguments == null || arguments.length != 1) {
            throw new EasyDataMockException("Illegal uniqueKeyByInstance() params");
        }

        final Class<?> target = (Class<?>) arguments[0];

        final List<Object> keyList = new ArrayList<>();
        keyList.add(target);
        if (isPrimitiveWrapper(target)) {
            keyList.add(wrapperToPrimitive(target));
        }

        return keyList;
    }

    @Override
    protected Object uniqueKey(Class<?>... args) {
        if (args == null || args.length != 1) {
            throw new EasyDataMockException("Illegal uniqueKeyByInstance() params");
        }

        return args[0];
    }
}
