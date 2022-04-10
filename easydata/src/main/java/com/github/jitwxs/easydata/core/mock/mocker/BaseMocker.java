package com.github.jitwxs.easydata.core.mock.mocker;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.core.mock.mocker.implicit.ArrayMocker;
import com.github.jitwxs.easydata.core.mock.mocker.implicit.ClassMocker;
import com.github.jitwxs.easydata.core.mock.mocker.implicit.GenericMocker;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 13:08
 */
public class BaseMocker<T> implements IMocker<T> {
    private final Type type;

    private final Type[] genericTypes;

    public BaseMocker(Type type, Type... genericTypes) {
        this.type = type;
        this.genericTypes = genericTypes;
    }

    @Override
    public T mock(MockConfig mockConfig) {
        IMocker<?> mocker;
        if (type instanceof ParameterizedType) {
            mocker = new GenericMocker((ParameterizedType) type);
        } else if (type instanceof GenericArrayType) {
            mocker = new ArrayMocker(type);
        } else if (type instanceof TypeVariable) {
            final String name = ((TypeVariable) type).getName();
            mocker = new BaseMocker(mockConfig.getTypeVariableCache().get(name));
        } else {
            mocker = new ClassMocker((Class<?>) type, genericTypes);
        }
        return (T) mocker.mock(mockConfig);
    }

}