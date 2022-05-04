package io.github.jitwxs.easydata.core.verify.impl;

import io.github.jitwxs.easydata.common.enums.GatherEnum;
import io.github.jitwxs.easydata.core.verify.VerifyInstance;
import io.github.jitwxs.easydata.core.verify.comp.BaseComp;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import org.apache.commons.lang3.ClassUtils;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.RecursiveComparisonAssert;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static io.github.jitwxs.easydata.core.verify.EasyVerify.COLLECTION_TYPE_VERIFY;
import static io.github.jitwxs.easydata.core.verify.EasyVerify.MAP_TYPE_VERIFY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:38
 */
public class BeanTypeVerify {
    public void check(Object a, Object b, VerifyInstance instance) {
        final Class<?> classA = a.getClass(), classB = b.getClass();

        // 类型强校验
        if (!instance.isIgnoreClassDiff()) {
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
                    COLLECTION_TYPE_VERIFY.check((Collection) a, (Collection) b, instance);
                    break;
                case MAP:
                    MAP_TYPE_VERIFY.check((Map) a, (Map) b, instance);
                    break;
            }
        } else {
            this.doOneEquals(a, b, instance);
        }
    }

    public void doOneEquals(Object a, Object b, VerifyInstance instance) {
        final Class<?> target = a.getClass();

        // 类型统一
        if (target != b.getClass()) {
            b = alignmentClass(target, b);
        }

        if (isEnableRecursiveCompare(target)) {
            RecursiveComparisonAssert<?> anAssert = assertThat(a).usingRecursiveComparison();

            final String[] ignoreFields = instance.toIgnoreFields();
            if (ignoreFields.length > 0) {
                anAssert = anAssert.ignoringFields(ignoreFields);
            }

            final String[] validateFields = instance.toValidateFields();
            if (validateFields.length > 0) {
                anAssert = anAssert.comparingOnlyFields(validateFields);
            }

            for (Map.Entry<Class<?>, BaseComp> entry : instance.getCompConfigs().entrySet()) {
                anAssert = anAssert.withComparatorForType(entry.getValue(), entry.getKey());
            }

            anAssert.isEqualTo(b);
        } else {
            ObjectAssert<Object> anAssert = assertThat(a);

            final BaseComp comp = instance.getCompConfigs().get(target);
            if (comp != null) {
                anAssert = anAssert.usingComparator(comp);
            }

            anAssert.isEqualTo(b);
        }
    }

    /**
     * 类型统一
     */
    private Object alignmentClass(Class<?> target, Object value) {
        return ProviderFactory.delegate(ConvertProvider.class).convert(value, target);
    }

    /**
     * 是否对字段起用递归比较
     *
     * @param target 类型
     * @return true: 启用; false: 不启用
     */
    public boolean isEnableRecursiveCompare(final Class<?> target) {
        // 基本类型或包装类型，无需递归
        if (ClassUtils.isPrimitiveOrWrapper(target)) {
            return false;
        }

        // 集合复杂类型，需要递归
        if (GatherEnum.delegate(target).isPresent()) {
            return true;
        }

        // 其他 java 原生对象，无需递归
        if (target.getPackage().getName().startsWith("java.")) {
            return false;
        }

        return true;
    }
}
