package io.github.jitwxs.easydata.core.mock;

import io.github.jitwxs.easydata.common.bean.IgnoreBean;
import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.enums.DataTypeEnum;
import io.github.jitwxs.easydata.common.enums.FileFormatEnum;
import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
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

import static io.github.jitwxs.easydata.common.util.TimeUtils.dateToLdt;
import static io.github.jitwxs.easydata.common.util.TimeUtils.ldtToMs;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class EasyMockTest {

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
        functions.add(i -> EasyMock.run(clazz));

        // 方法二（不支持基本数据类型）
        if (!clazz.isPrimitive()) {
            functions.add(i -> EasyMock.run(this.newTypeKit(clazz)));
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
                i -> EasyMock.run(clazz), // 方法一
                i -> EasyMock.run(this.newTypeKit(clazz)) // 方法二
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
                i -> EasyMock.run(String.class, mockConfig), // 方法一
                i -> EasyMock.run(new TypeKit<String>() {
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
                i -> EasyMock.run(clazz, mockConfig), // 方法一
                i -> EasyMock.run(this.newTypeKit(clazz), mockConfig) // 方法二
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
                Arguments.of("普通集合", (Function<Integer, Object>) i -> EasyMock.run(new TypeKit<List<Integer>>() {
                })),
                Arguments.of("LinkedList", (Function<Integer, Object>) i -> EasyMock.run(new TypeKit<LinkedList<Double>>() {
                })),
                Arguments.of("数组集合", (Function<Integer, Object>) i -> EasyMock.run(new TypeKit<List<Integer[]>>() {
                })),
                Arguments.of("对象集合", (Function<Integer, Object>) i -> EasyMock.run(new TypeKit<List<MockConfig>>() {
                })),
                Arguments.of("普通Map", (Function<Integer, Object>) i -> EasyMock.run(new TypeKit<Map<Integer, Boolean>>() {
                })),
                Arguments.of("集合嵌套Map", (Function<Integer, Object>) i -> EasyMock.run(new TypeKit<Map<List<Map<Integer, String[][]>>, Map<Set<String>, Double[]>>>() {
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
        final IgnoreBean bean = EasyMock.run(IgnoreBean.class);

        assertNotNull(bean);
        assertNull(bean.getIgnore());
        assertNotNull(bean.getNotIgnore());
    }

    @Test
    public void testClassicCase() {
        final Integer integer = EasyMock.run(Integer.class);
        System.out.println(integer);

        final double aDouble = EasyMock.run(double.class);
        System.out.println(aDouble);

        final char[] chars = EasyMock.run(char[].class);
        System.out.println(chars);

        final boolean[][][] booleans = EasyMock.run(boolean[][][].class);
        System.out.println(Arrays.asList(booleans));

        final List<String> stringList = EasyMock.run(new TypeKit<List<String>>() {
        });
        System.out.println(stringList);

        final Map<Boolean, Set<BigDecimal>> listMap = EasyMock.run(new TypeKit<Map<Boolean, Set<BigDecimal>>>() {
        });
        System.out.println(listMap);
    }

    @Test
    public void testMockConfigForInt() {
        final int value = EasyMock.run(int.class, new MockConfig().setIntRange(100, 105));
        assertTrue(value >= 100 && value < 105);
    }

    @Test
    public void testMockConfigForDate() {
        final LocalDateTime startLDT = LocalDateTime.of(2008, 8, 8, 20, 20, 20);
        final LocalDateTime endLDT = LocalDateTime.now();

        final MockConfig dateMockConfig = new MockConfig().setDateRange(ldtToMs(startLDT), ldtToMs(endLDT));

        final LocalDateTime ldt = EasyMock.run(LocalDateTime.class, dateMockConfig);
        assertTrue(ldt.isAfter(startLDT) && ldt.isBefore(endLDT));

        final LocalDateTime ldt1 = dateToLdt(EasyMock.run(Date.class, dateMockConfig));
        assertTrue(ldt1.isAfter(startLDT) && ldt1.isBefore(endLDT));
    }

    @Test
    public void testMockConfigForString() {
        final String number = EasyMock.run(String.class, new MockConfig().setStringEnum(MockStringEnum.NUMBER));
        assertTrue(StringUtils.isNumeric(number));
    }
}