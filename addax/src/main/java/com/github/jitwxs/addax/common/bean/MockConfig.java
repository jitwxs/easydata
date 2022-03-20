package com.github.jitwxs.addax.common.bean;

import com.github.jitwxs.addax.common.enums.MockStringEnum;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:46
 */
@Data
public class MockConfig {
    private byte[] byteRange = {0, 127};

    private short[] shortRange = {0, 1000};

    private int[] intRange = {0, 10000};

    private long[] longRange = {0L, 10000L};

    private float[] floatRange = {0.0f, 10000.00f};

    /**
     * the pattern describing the date and time format:
     * <pre>
     *     yyyy-MM-dd HH:mm:ss.SSS
     * </pre>
     */
    private String[] dateRange = {"1970-01-01 08:00:00.000", "2222-12-31 23:59:59.999"};

    private int[] sizeRange = {1, 5};

    private MockStringEnum stringEnum = MockStringEnum.UUID;

    private Map<String, Type> typeVariableCache = new HashMap<>();

    public MockConfig init(Type type) {
        if (type instanceof ParameterizedType) {
            Class clazz = (Class) ((ParameterizedType) type).getRawType();
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            TypeVariable[] typeVariables = clazz.getTypeParameters();
            if (typeVariables != null && typeVariables.length > 0) {
                for (int index = 0; index < typeVariables.length; index++) {
                    typeVariableCache.put(typeVariables[index].getName(), types[index]);
                }
            }
        }
        return this;
    }

    public int nexSize() {
        return sizeRange[0] + RandomUtils.nextInt(0, sizeRange[1] - sizeRange[0] + 1);
    }
}
