package com.github.jitwxs.addax.core.verify;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.enums.MockStringEnum;
import com.github.jitwxs.addax.sample.bean.Student;
import com.github.jitwxs.addax.sample.bean.UserInfo;
import com.github.jitwxs.addax.sample.enums.SexEnum;
import com.github.jitwxs.addax.sample.message.EnumProto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.jitwxs.addax.core.mock.Mock.run;
import static com.github.jitwxs.addax.core.verify.Verify.go;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 相同简单类型的 Equals 比较
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 18:02
 */
public class SameSimpleClassITypeVerifyTest {
    private static Stream<Arguments> classNotMatchScenario() {
        return Stream.of(
                Arguments.of(emptyList(), emptyMap()), // list和map
                Arguments.of(new String[0], emptySet()) // 数组和set
        );
    }

    @ParameterizedTest
    @MethodSource("classNotMatchScenario")
    @DisplayName("测试类不匹配")
    public void testClassNotMatch(final Object except, final Object actual) {
        final Executable executable = () -> go(except, actual).run();
        assertThrows(AssertionError.class, executable);
    }

    private static Stream<Arguments> enumScenario() {
        return Stream.of(
                Arguments.of(true, EnumProto.SexEnum.MALE, EnumProto.SexEnum.MALE), // proto枚举相等
                Arguments.of(false, EnumProto.SexEnum.MALE, EnumProto.SexEnum.FEMALE), // proto枚举不等
                Arguments.of(true, SexEnum.FEMALE, SexEnum.FEMALE), // 普通枚举相等
                Arguments.of(false, SexEnum.FEMALE, SexEnum.MALE) // 普通枚举不等
        );
    }

    @ParameterizedTest
    @MethodSource("enumScenario")
    @DisplayName("枚举类型比较")
    public void testEnumEquals(final boolean isEquals, final Object except, final Object actual) {
        final Executable executable = () -> go(except, actual).run();
        if (isEquals) {
            assertDoesNotThrow(executable);
        } else {
            assertThrows(AssertionError.class, executable);
        }
    }

    @Test
    @DisplayName("测试Proto对象相等")
    public void testProtoEquals() {
        final MockConfig mockConfig = new MockConfig();
        mockConfig.setStringEnum(MockStringEnum.CN_NAME);

        final Student student = new Student();
        student.setSex(EnumProto.SexEnum.MALE);
        student.setName(run(String.class, mockConfig));

        final Student student2 = new Student();
        student2.setSex(EnumProto.SexEnum.MALE);
        student2.setName(student.getName());

        assertDoesNotThrow(() -> go(student, student2).run());
    }

    @Test
    @DisplayName("测试Proto对象相等 | 忽略字段")
    public void testProtoEqualsIgnoreField() {
        final MockConfig mockConfig = new MockConfig();
        mockConfig.setStringEnum(MockStringEnum.CN_NAME);

        final Student student = new Student();
        student.setSex(EnumProto.SexEnum.MALE);
        student.setName(run(String.class, mockConfig));

        final Student student2 = new Student();
        student2.setSex(EnumProto.SexEnum.MALE);
        student2.setName(run(String.class, mockConfig));

        assertDoesNotThrow(() -> go(student, student2).ignoredFields("name").run());
    }

    @Test
    @DisplayName("测试Proto对象相等 | 指定字段")
    public void testProtoEqualsSpecifyField() {
        final UserInfo user1 = UserInfo.builder()
                ._id(run(String.class, new MockConfig().setStringEnum(MockStringEnum.CN_NAME)))
                .age(15)
                .email(run(String.class, new MockConfig().setStringEnum(MockStringEnum.EMAIL)))
                .build();

        final UserInfo user2 = UserInfo.builder()
                ._id(run(String.class, new MockConfig().setStringEnum(MockStringEnum.CN_NAME)))
                .age(15)
                .email(run(String.class, new MockConfig().setStringEnum(MockStringEnum.EMAIL)))
                .build();

        assertDoesNotThrow(() -> go(user1, user2).validateFields("age").run());
    }

    @Test
    @DisplayName("测试Proto对象相等 | 忽略字段 | 指定字段")
    public void testProtoEqualsIgnoreAndSpecifyField() {
        final UserInfo user1 = UserInfo.builder()
                ._id(run(String.class, new MockConfig().setStringEnum(MockStringEnum.CN_NAME)))
                .age(15)
                .email(run(String.class, new MockConfig().setStringEnum(MockStringEnum.EMAIL)))
                .build();

        final UserInfo user2 = UserInfo.builder()
                ._id(run(String.class, new MockConfig().setStringEnum(MockStringEnum.CN_NAME)))
                .age(15)
                .email(run(String.class, new MockConfig().setStringEnum(MockStringEnum.EMAIL)))
                .build();

        assertDoesNotThrow(() -> go(user1, user2).ignoredFields("_id").validateFields("age").run());
    }
}
