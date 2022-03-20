package com.github.jitwxs.addax.core.mock.mocker.implicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.BaseMocker;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import lombok.AllArgsConstructor;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 数组模拟器
 */
@AllArgsConstructor
public class ArrayMocker implements IMocker<Object> {
    private final Type type;

    @Override
    public Object mock(MockConfig mockConfig) {
        if (type instanceof GenericArrayType) {
            // 创建有参数化的数组
            return createGenericArray(mockConfig);
        } else {
            return createArray(mockConfig);
        }
    }

    private Object createArray(MockConfig mockConfig) {
        int size = mockConfig.nexSize();
        Class<?> componentClass = ((Class<?>) type).getComponentType();

        final Object result = Array.newInstance(componentClass, size);

        final BaseMocker<?> baseMocker = new BaseMocker<>(componentClass);
        for (int index = 0; index < size; index++) {
            Array.set(result, index, baseMocker.mock(mockConfig));
        }

        return result;
    }

    // TODO 代码还需要整理
    // 由于GenericArrayType无法获得Class，所以递归创建多维数组
    private Object createGenericArray(MockConfig mockConfig) {
        GenericArrayType genericArrayType = (GenericArrayType) this.type;
        // 递归获取该数组的维数，以及最后的Class类型
        Map<Integer, Map<Class, Type[]>> map = map(mockConfig, genericArrayType, 0);
        Entry<Integer, Map<Class, Type[]>> entry = map.entrySet().iterator().next();
        Entry<Class, Type[]> baseEntry = entry.getValue().entrySet().iterator().next();
        int[] dimensions = new int[entry.getKey()];
        for (int index = 0; index < dimensions.length; index++) {
            dimensions[index] = mockConfig.nexSize();
        }
        // 创建多维数组每种维度的对象
        List<Object> list = new ArrayList<>(dimensions.length);
        Class clazz = baseEntry.getKey();
        for (int index = dimensions.length - 1; index >= 0; index--) {
            Object array = Array.newInstance(clazz, dimensions[index]);
            list.add(array);
            clazz = array.getClass();
        }
        // 实例化多维数组
        Object baseResult = new BaseMocker(baseEntry.getKey(), baseEntry.getValue()).mock(mockConfig);
        for (int i = 0; i < list.size(); i++) {
            Object array = list.get(i);
            for (int j = 0; j < dimensions[dimensions.length - i - 1]; j++) {
                Array.set(array, j, baseResult);
            }
            baseResult = array;
        }
        return baseResult;
    }

    private Map<Integer, Map<Class, Type[]>> map(MockConfig mockConfig, GenericArrayType genericArrayType, int dimension) {
        Map<Integer, Map<Class, Type[]>> result = new HashMap<>();
        Type componentType = genericArrayType.getGenericComponentType();
        dimension++;
        if (componentType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) componentType;
            Map<Class, Type[]> map = new HashMap<>();
            map.put((Class) parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
            result.put(dimension, map);
            return result;
        }
        if (componentType instanceof GenericArrayType) {
            return map(mockConfig, (GenericArrayType) componentType, dimension);
        }
        if (componentType instanceof TypeVariable) {
            Map<Class, Type[]> map = new HashMap<>();
            final String name = ((TypeVariable) componentType).getName();
            map.put((Class) mockConfig.getTypeVariableCache().get(name), null);
            result.put(dimension, map);
            return result;
        }
        Map<Class, Type[]> map = new HashMap<>();
        map.put((Class) componentType, null);
        result.put(dimension, map);
        return result;
    }

}
