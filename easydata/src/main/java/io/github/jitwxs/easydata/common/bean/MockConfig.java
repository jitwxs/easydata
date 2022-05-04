package io.github.jitwxs.easydata.common.bean;

import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import lombok.Getter;
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
@Getter
public class MockConfig {
    private byte[] byteRange = {0, 127};

    private short[] shortRange = {0, 1000};

    private int[] intRange = {0, 10000};

    private long[] longRange = {0L, 10000L};

    private float[] floatRange = {0.0f, 10000.00f};

    /**
     * timestamp start and end for mock date
     */
    private long[] dateRange = {1, 7983849599999L};

    private int[] sizeRange = {1, 5};

    private MockStringEnum stringEnum = MockStringEnum.ENGLISH;

    private final Map<String, Type> typeVariableCache = new HashMap<>();

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

    public MockConfig setStringEnum(final MockStringEnum stringEnum) {
        this.stringEnum = stringEnum;
        return this;
    }

    public MockConfig setByteRange(byte startInclusive, byte endExclusive) {
        if (endExclusive < startInclusive) {
            throw new EasyDataMockException("MockConfig#setByteRange params illegal");
        }

        this.byteRange = new byte[]{startInclusive, endExclusive};
        return this;
    }

    public MockConfig setShortRange(short startInclusive, short endExclusive) {
        if (endExclusive < startInclusive) {
            throw new EasyDataMockException("MockConfig#setShortRange params illegal");
        }

        this.shortRange = new short[]{startInclusive, endExclusive};
        return this;
    }

    /**
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     */
    public MockConfig setIntRange(int startInclusive, int endExclusive) {
        if (endExclusive < startInclusive) {
            throw new EasyDataMockException("MockConfig#setIntRange params illegal");
        }

        this.intRange = new int[]{startInclusive, endExclusive};
        return this;
    }

    public MockConfig setLongRange(long startInclusive, long endExclusive) {
        if (endExclusive < startInclusive) {
            throw new EasyDataMockException("MockConfig#setLongRange params illegal");
        }

        this.longRange = new long[]{startInclusive, endExclusive};
        return this;
    }

    public MockConfig setFloatRange(float startInclusive, float endExclusive) {
        if (endExclusive < startInclusive) {
            throw new EasyDataMockException("MockConfig#setFloatRange params illegal");
        }

        this.floatRange = new float[]{startInclusive, endExclusive};
        return this;
    }

    public MockConfig setDateRange(long startInclusive, long endExclusive) {
        if (startInclusive < 0 || endExclusive < 0) {
            throw new EasyDataMockException("MockConfig#setDateRange params not allow less zero");
        }
        if (endExclusive < startInclusive) {
            throw new EasyDataMockException("MockConfig#setDateRange params illegal");
        }

        this.dateRange = new long[]{startInclusive, endExclusive};
        return this;
    }

    public MockConfig setSizeRange(int startInclusive, int endExclusive) {
        if (endExclusive < startInclusive) {
            throw new EasyDataMockException("MockConfig#setSizeRange params illegal");
        }

        this.sizeRange = new int[]{startInclusive, endExclusive};
        return this;
    }
}
