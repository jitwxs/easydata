package com.github.jitwxs.addax.core.mock;

import com.github.jitwxs.addax.common.bean.IgnoreBean;
import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.enums.DataTypeEnum;
import com.github.jitwxs.addax.common.enums.FileFormatEnum;
import com.github.jitwxs.addax.common.enums.MockStringEnum;
import com.github.jitwxs.addax.common.util.ObjectUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.commons.lang3.ClassUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MockTest {

    private static Stream<Class<?>> explicitScenario() {
        return Stream.of(
                Byte.class, byte.class,
                Integer.class, int.class,
                Short.class, short.class,
                Float.class, float.class,
                Double.class, double.class,
                Boolean.class, boolean.class,
                Character.class, char.class,
                BigInteger.class, BigDecimal.class,
                String.class, Date.class,
                LocalDate.class, LocalDateTime.class
        );
    }

    @ParameterizedTest
    @MethodSource("explicitScenario")
    @DisplayName("Mock显式指定的类型")
    public void testExplicitMock(final Class<?> clazz) {
        final List<Function<Integer, Object>> functions = new ArrayList<>();

        // 方法一
        functions.add(i -> Mock.run(clazz));

        // 方法二（不支持基本数据类型）
        if (!clazz.isPrimitive()) {
            functions.add(i -> Mock.run(this.newTypeKit(clazz)));
        }

        for (Function<Integer, Object> function : functions) {
            final Object value = function.apply(0);

            assertNotNull(value);
            assertEquals(ClassUtils.primitiveToWrapper(clazz), value.getClass());
        }
    }

    private static Stream<Class<? extends Enum<?>>> enumScenario() {
        return Stream.of(
                DataTypeEnum.class, FileFormatEnum.class, MockStringEnum.class
        );
    }

    @ParameterizedTest
    @MethodSource("enumScenario")
    @DisplayName("Mock枚举类型")
    public void testEnumMock(final Class<? extends Enum<?>> clazz) {
        final List<Function<Integer, Object>> functions = Arrays.asList(
                i -> Mock.run(clazz), // 方法一
                i -> Mock.run(this.newTypeKit(clazz)) // 方法二
        );

        for (Function<Integer, Object> function : functions) {
            final Object value = function.apply(0);

            assertNotNull(value);
            assertEquals(clazz, value.getClass());
        }
    }

    @ParameterizedTest
    @EnumSource(MockStringEnum.class)
    @DisplayName("测试Mock不同类型的字符串")
    public void testStringTypeMock(final MockStringEnum mockStringEnum) {
        final MockConfig mockConfig = new MockConfig();
        mockConfig.setStringEnum(mockStringEnum);

        final List<Function<Integer, String>> functions = Arrays.asList(
                i -> Mock.run(String.class, mockConfig), // 方法一
                i -> Mock.run(new TypeKit<String>() {
                }, mockConfig) // 方法二
        );

        for (Function<Integer, String> function : functions) {
            final String value = function.apply(0);
            System.out.println(value);
        }
    }

    private static Stream<Arguments> arrayScenario() {
        return Stream.concat(explicitScenario(), enumScenario()).map(e -> {
            // 多维数组
            final int[] dimConfigs = new int[nextInt(1, 4)];
            Arrays.fill(dimConfigs, 0);
            return arguments(Array.newInstance(e, dimConfigs).getClass(), nextInt(1, 15));
        });
    }

    @ParameterizedTest
    @MethodSource("arrayScenario")
    @DisplayName("Mock任意维度数组")
    public void testArrayMock(final Class<?> clazz, final int size) {
        final MockConfig mockConfig = Mockito.spy(new MockConfig());
        Mockito.when(mockConfig.nexSize()).thenReturn(size);

        final List<Function<Integer, Object>> functions = Arrays.asList(
                i -> Mock.run(clazz, mockConfig), // 方法一
                i -> Mock.run(this.newTypeKit(clazz), mockConfig) // 方法二
        );

        for (Function<Integer, Object> function : functions) {
            final Object value = function.apply(0);

            assertNotNull(value);
            assertTrue(value.getClass().isArray());
            assertEquals(Array.getLength(value), size);
        }
    }

    private static Stream<Arguments> typeReferenceScenario() {
        return Stream.of(
                Arguments.of("普通集合", (Function<Integer, Object>) i -> Mock.run(new TypeKit<List<Integer>>() {
                })),
                Arguments.of("LinkedList", (Function<Integer, Object>) i -> Mock.run(new TypeKit<LinkedList<Double>>() {
                })),
                Arguments.of("数组集合", (Function<Integer, Object>) i -> Mock.run(new TypeKit<List<Integer[]>>() {
                })),
                Arguments.of("对象集合", (Function<Integer, Object>) i -> Mock.run(new TypeKit<List<MockConfig>>() {
                })),
                Arguments.of("普通Map", (Function<Integer, Object>) i -> Mock.run(new TypeKit<Map<Integer, Boolean>>() {
                })),
                Arguments.of("集合嵌套Map", (Function<Integer, Object>) i -> Mock.run(new TypeKit<Map<List<Map<Integer, String[][]>>, Map<Set<String>, Double[]>>>() {
                }))
        );
    }

    @ParameterizedTest
    @MethodSource("typeReferenceScenario")
    @DisplayName("TypeReference处理集合类型")
    public void testTypeReference(final String scenarioName, Function<Integer, Object> function) {
        final Object value = function.apply(0);

        assertNotNull(value);

        System.out.printf("Scenario: %s, Value: %s", scenarioName, value);
    }

    /**
     * 构造 {@link TypeKit} 实例
     * <p>
     * 此方法仅为方便测试所用，实际流程请使用
     * <pre>
     *     TypeKit<T> typeKit = new TypeKit<T>(){};
     * </pre>
     */
    private TypeKit<?> newTypeKit(final Class<?> referenceClass) {
        TypeDescription.Generic genericSuperClass =
                TypeDescription.Generic.Builder.parameterizedType(TypeKit.class, referenceClass).build();

        final Class<?> virtualClass = new ByteBuddy()
                .subclass(genericSuperClass)
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        return (TypeKit<?>) ObjectUtils.create(virtualClass);
    }

    @Test
    public void testIgnore() {
        final IgnoreBean bean = Mock.run(IgnoreBean.class);

        assertNotNull(bean);
        assertNull(bean.getIgnore());
        assertNotNull(bean.getNotIgnore());
    }
}