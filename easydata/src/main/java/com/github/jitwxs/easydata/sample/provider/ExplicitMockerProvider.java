package com.github.jitwxs.easydata.sample.provider;

import com.github.jitwxs.easydata.sample.common.exception.EasyDataMockException;
import com.github.jitwxs.easydata.sample.common.util.ObjectUtils;
import com.github.jitwxs.easydata.sample.common.util.ReflectionUtils;
import com.github.jitwxs.easydata.sample.core.mock.mocker.IMocker;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    protected Object uniqueKeyByInstance(IMocker instance) {
        final Class<? extends IMocker> clazz = instance.getClass();

        final Type[] arguments = ReflectionUtils.getGenericInterface0Class(clazz);

        if (arguments == null || arguments.length != 1) {
            throw new EasyDataMockException("Illegal uniqueKeyByInstance() params");
        }

        return arguments[0];
    }

    @Override
    protected Object uniqueKey(Class<?>... args) {
        if (args == null || args.length != 1) {
            throw new EasyDataMockException("Illegal uniqueKeyByInstance() params");
        }

        return args[0];
    }
}
