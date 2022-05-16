package io.github.jitwxs.easydata.core.convert;

import io.github.jitwxs.easydata.core.verify.EasyVerify;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

/**
 * 关于时间类型 convert
 *
 * @author jitwxs@foxmail.com
 * @since 2022-05-16 13:12
 */
public class AboutTimeConvertTest {

    private ConvertProvider provider;

    @BeforeEach
    public void beforeEach() {
        provider = ProviderFactory.delegate(ConvertProvider.class);
    }

    private static Stream<Object> enumScenario() {
        return Stream.of(
                new Date(),
                LocalDateTime.now(),
                LocalDate.now(),
                "2022-05-12 12:13:00",
                "2022-05-12 12:13:00.123",
                "2022-05-12 12:13:00.45",
                1652678066915L
        );
    }

    @ParameterizedTest
    @MethodSource("enumScenario")
    @DisplayName("Mock枚举类型")
    public void testTimeConvert(final Object source) {
        final Class<?>[] targetClassArray = new Class[]{
                Date.class,
                LocalDateTime.class,
                LocalDate.class,
                Long.class
        };

        for (Class<?> target : targetClassArray) {
            final Object actual = provider.convert(source, target);

            EasyVerify.notNull(actual);

            System.out.printf("%s --> %s, result: %s\n", source, target, actual);
        }
    }
}
