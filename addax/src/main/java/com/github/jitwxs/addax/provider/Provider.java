package com.github.jitwxs.addax.provider;

import com.github.jitwxs.addax.common.exception.AddaxLoaderException;
import com.github.jitwxs.addax.common.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:54
 */
public abstract class Provider<T> {
    private final Map<Object, T> instanceMap;

    /**
     * 加载原生实现
     */
    protected abstract List<T> loadNative();

    protected abstract Object uniqueKeyByInstance(T instance);

    protected abstract Object uniqueKey(Class<?>... args);

    @SuppressWarnings("unchecked")
    public Provider() {
        this.instanceMap = new HashMap<>();

        final Class<T> targetClazz = (Class<T>) ReflectionUtils.getGenericSuperClass(this.getClass())[0];
        final Consumer<List<T>> addToMemory = instances ->
                emptyIfNull(instances).stream().filter(Objects::nonNull).forEach(e -> this.instanceMap.put(this.uniqueKeyByInstance(e), e));

        addToMemory.accept(this.loadNative());
        addToMemory.accept(this.loadSpi(this.getClass().getClassLoader(), targetClazz));
    }

    public T delegate(final Class<?>... args) {
        final T instance = this.instanceMap.get(uniqueKey(args));
        if (instance == null) {
            throw new AddaxLoaderException(String.format("Not Exist Instance In %s, params: %s", getClass(), Arrays.toString(args)));
        }

        return instance;
    }

    /**
     * 加载 SPI 实现
     */
    private List<T> loadSpi(final ClassLoader classLoader, final Class<T> loadClass) {
        final List<T> resultList = new ArrayList<>();

        for (T t : ServiceLoader.load(loadClass, classLoader)) {
            if (Modifier.isInterface(t.getClass().getModifiers())) {
                continue;
            }

            resultList.add(t);
        }

        return resultList;
    }
}
