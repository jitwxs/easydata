package com.github.jitwxs.addax.provider;

import com.github.jitwxs.addax.common.util.ReflectionUtils;

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
     * 加载原生实现
     */
    protected abstract List<T> loadNative();

    /**
     * 根据实例，获取唯一键
     */
    protected abstract Object uniqueKeyByInstance(T instance);

    /**
     * 根据参数，生成唯一键
     */
    protected abstract Object uniqueKey(UK... args);

    protected final Consumer<Collection<T>> doRegister = instances ->
            emptyIfNull(instances).stream().filter(Objects::nonNull).forEach(e -> this.instanceMap.put(this.uniqueKeyByInstance(e), e));

    public Provider() {
        final Class<T> targetClazz = (Class<T>) ReflectionUtils.getGenericSuperClass(this.getClass())[0];

        doRegister.accept(this.loadNative());
        doRegister.accept(this.loadSpi(this.getClass().getClassLoader(), targetClazz));
    }

    public final T delegate(final UK... args) {
        return this.instanceMap.get(uniqueKey(args));
    }

    /**
     * 加载 SPI 实现
     */
    protected List<T> loadSpi(final ClassLoader classLoader, final Class<T> loadClass) {
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
