package io.github.jitwxs.easydata.common.bean;

import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.core.mock.BeanMockInterceptor;
import lombok.Getter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:46
 */
@Getter
public class MockConfig {
    /**
     * 对于对象类型，mock 其中的字段时，可以通过 {@code contrastClass} 的字段类型来指定 mock 出什么类型的数据，常用于通信协议的转换
     * <p>
     * 例如: 对于 string 类型字段的 mock，可能是 double，也可能是 timestamp，默认情况下只能根据 {@link #stringEnum} 来生成，
     * 如果该字段在 {@code contrastClass} 中也存在，那么就可以使用 {@code contrastClass} 中的类型进行生成。
     * <p>
     * 例如：
     * <p>
     * 存在 student 对象的通信协议如下，所有字段使用 string 类型传输。使用 {@link #stringEnum} 生成的数据类型都是一致的（数字 or 时间戳）。
     *
     * <pre>
     *     message Student {
     *         string age = 0;
     *         string name = 1;
     *         string createTime = 2;
     *     }
     * </pre>
     *
     * <pre>
     *     class Student {
     *         int age;
     *         string name;
     *         long createTime;
     *     }
     * </pre>
     */
    private Class<?> contrastClass;

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

    /**
     * TypeVariable缓存
     */
    private final Map<String, Type> typeVariableCache = new HashMap<>();

    /**
     * Bean缓存
     */
    private final Map<String, Object> beanCache = new HashMap<>();

    private final Map<Class<?>, BeanMockInterceptor<?>> beanMockInterceptorMap = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public MockConfig init(Type type) {
        if (type instanceof ParameterizedType) {
            Class clazz = (Class) ((ParameterizedType) type).getRawType();
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            TypeVariable[] typeVariables = clazz.getTypeParameters();
            if (typeVariables.length > 0) {
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

    public MockConfig contrastClass(final Class<?> contrast) {
        this.contrastClass = contrast;
        return this;
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
     * @return mockConfig instance in chain invoke
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

    /**
     * 注册 BeanMocker 拦截器
     *
     * @param clazz       类型
     * @param interceptor BeanMocker 拦截器
     * @param <T>         类型泛型
     * @return mockConfig instance in chain invoke
     */
    public <T> MockConfig registerBeanMockerInterceptor(Class<T> clazz, BeanMockInterceptor<T> interceptor) {
        this.beanMockInterceptorMap.put(clazz, interceptor);

        return this;
    }

    /**
     * 获取 BeanMocker 拦截器
     *
     * @param clazz 类型
     * @param <T>   类型泛型
     * @return BeanMocker 拦截器
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<BeanMockInterceptor<T>> getBeanMockerInterceptor(Class<T> clazz) {
        if (MapUtils.isEmpty(this.beanMockInterceptorMap)) {
            return Optional.empty();
        }

        final BeanMockInterceptor<T> interceptor = (BeanMockInterceptor<T>) this.beanMockInterceptorMap.get(clazz);

        return Optional.ofNullable(interceptor);
    }
}
