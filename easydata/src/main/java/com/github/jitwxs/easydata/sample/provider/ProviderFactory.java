package com.github.jitwxs.easydata.sample.provider;

import com.github.jitwxs.easydata.sample.EasyDataEntrance;
import com.github.jitwxs.easydata.sample.common.exception.EasyDataException;
import com.github.jitwxs.easydata.sample.common.util.ObjectUtils;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 18:35
 */
@SuppressWarnings("rawtypes")
public class ProviderFactory extends EasyDataEntrance {
    private static final Map<Class<? extends Provider>, Provider> providerMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections(Provider.class.getPackage().getName());

        reflections.getSubTypesOf(Provider.class).forEach(c -> {
            final int modifiers = c.getModifiers();

            if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
                return;
            }

            providerMap.put(c, ObjectUtils.create(c));
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends Provider> T delegate(final Class<T> clazz) {
        final T provider = (T) providerMap.get(clazz);
        if (provider == null) {
            throw new EasyDataException("Not Exist " + clazz + " Instance");
        }

        return provider;
    }
}
