package com.github.jitwxs.easydata.provider;

import com.github.jitwxs.easydata.common.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:54
 */
public abstract class Provider<T, UK> {
    private final Map<Object, T> instanceMap = new HashMap<>();

    /**
     * loading native implements
     *
     * @return native implements
     */
    protected abstract List<T> loadNative();

    /**
     * Generate unique key according to instance
     *
     * @param instance instance
     * @return unique key
     */
    protected abstract Object uniqueKeyByInstance(T instance);

    /**
     * Generate unique key according to parameters
     *
     * @param args parameters
     * @return unique key
     */
    protected abstract Object uniqueKey(UK... args);

    protected final Consumer<Collection<T>> doRegister = instances ->
            emptyIfNull(instances).stream().filter(Objects::nonNull).forEach(e -> this.instanceMap.put(this.uniqueKeyByInstance(e), e));

    public Provider() {
        final Class<T> targetClazz = (Class<T>) ReflectionUtils.getGenericSuperClass(this.getClass())[0];

        doRegister.accept(this.loadNative());
        doRegister.accept(this.loadSpi(this.getClass().getClassLoader(), targetClazz));
    }

    /**
     * Route the instance object according to the parameter list
     *
     * @param args parameter list
     * @return instance object
     */
    public final T delegate(final UK... args) {
        return this.instanceMap.get(uniqueKey(args));
    }

    /**
     * loading service provider interface implements
     *
     * @param classLoader The class loader to be used to load provider-configuration files
     *                    and provider classes, or <tt>null</tt> if the system class
     *                    loader (or, failing that, the bootstrap class loader) is to be used
     * @param target      The interface or abstract class representing the service
     * @return loading implement results
     */
    protected List<T> loadSpi(final ClassLoader classLoader, final Class<T> target) {
        final List<T> resultList = new ArrayList<>();

        for (T t : ServiceLoader.load(target, classLoader)) {
            if (Modifier.isInterface(t.getClass().getModifiers())) {
                continue;
            }

            resultList.add(t);
        }

        return resultList;
    }
}
