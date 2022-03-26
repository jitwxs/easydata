package com.github.jitwxs.addax.core.verify.impl;

import com.github.jitwxs.addax.common.enums.GatherEnum;
import com.github.jitwxs.addax.core.verify.VerifyContext;
import com.github.jitwxs.addax.provider.ConvertProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import org.assertj.core.api.RecursiveComparisonAssert;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.github.jitwxs.addax.core.verify.Verify.COLLECTION_TYPE_VERIFY;
import static com.github.jitwxs.addax.core.verify.Verify.MAP_TYPE_VERIFY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:38
 */
public class BeanTypeVerify {
    public void check(Object a, Object b, VerifyContext context) {
        final Class<?> classA = a.getClass(), classB = b.getClass();

        // 类型强校验
        if (!context.isIgnoreClassDiff()) {
            assertThat(a).hasSameClassAs(b);
        }

        final Optional<GatherEnum> gatherA = GatherEnum.delegate(classA);
        final Optional<GatherEnum> gatherB = GatherEnum.delegate(classB);

        // 一个复杂类型、一个非复杂类型
        if (gatherA.isPresent() ^ gatherB.isPresent()) {
            fail("Equals Failed, One Simple Class And One Gather Class");
        }

        if (gatherA.isPresent() && gatherB.isPresent()) {
            // 复杂类型转发
            final GatherEnum gatherEnum = gatherA.get();
            switch (gatherEnum) {
                case COLLECTION:
                    COLLECTION_TYPE_VERIFY.check((Collection) a, (Collection) b, context);
                    break;
                case MAP:
                    MAP_TYPE_VERIFY.check((Map) a, (Map) b, context);
                    break;
            }
        } else {
            this.doOneEquals(a, b, context);
        }
    }

    public Object alignmentClass(Class<?> target, Object value) {
        return ProviderFactory.delegate(ConvertProvider.class).convert(value, target);
    }

    public void doOneEquals(Object a, Object b, VerifyContext context) {
        final Class<?> target = a.getClass();

        // 类型统一
        if (target != b.getClass()) {
            b = alignmentClass(target, b);
        }

        // 比较
        RecursiveComparisonAssert<?> assert0 = assertThat(a).usingRecursiveComparison();

        final String[] ignoreFields = context.toIgnoreFields();
        if (ignoreFields.length > 0) {
            assert0 = assert0.ignoringFields(ignoreFields);
        }

        final String[] validateFields = context.toValidateFields();
        if (validateFields.length > 0) {
            assert0 = assert0.comparingOnlyFields(validateFields);
        }

        assert0.isEqualTo(b);
    }
}
