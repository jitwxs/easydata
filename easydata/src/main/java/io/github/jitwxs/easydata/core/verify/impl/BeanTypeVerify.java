package io.github.jitwxs.easydata.core.verify.impl;

import com.google.common.collect.Sets;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.ClassGroupEnum;
import io.github.jitwxs.easydata.common.enums.GatherEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataVerifyException;
import io.github.jitwxs.easydata.core.verify.VerifyInstance;
import io.github.jitwxs.easydata.core.verify.comp.EasyVerifyComp;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.assertj.core.api.RecursiveComparisonAssert;

import java.util.*;
import java.util.function.Function;

import static io.github.jitwxs.easydata.core.verify.EasyVerify.COLLECTION_TYPE_VERIFY;
import static io.github.jitwxs.easydata.core.verify.EasyVerify.MAP_TYPE_VERIFY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 20:38
 */
public class BeanTypeVerify {
    public void check(Object except, Object actual, VerifyInstance instance) {
        final Class<?> exceptClass = except.getClass(), actualClass = actual.getClass();

        // 类型强校验
        if (instance.getClassDiffVerifyStrategy() == null) {
            assertThat(actual).hasSameClassAs(except);
        }

        final Optional<GatherEnum> gatherA = GatherEnum.delegate(exceptClass);
        final Optional<GatherEnum> gatherB = GatherEnum.delegate(actualClass);

        // 一个复杂类型、一个非复杂类型
        if (gatherA.isPresent() ^ gatherB.isPresent()) {
            fail("Verify Failed, One Simple Class And One Gather Class");
        }

        if (gatherA.isPresent() && gatherB.isPresent()) {
            // 两个都是复杂类型
            final GatherEnum gatherEnum = gatherA.get();
            switch (gatherEnum) {
                case COLLECTION:
                    COLLECTION_TYPE_VERIFY.check((Collection) except, (Collection) actual, instance);
                    break;
                case MAP:
                    MAP_TYPE_VERIFY.check((Map) except, (Map) actual, instance);
                    break;
            }
        } else {
            // 两个都是非复杂类型
            this.doOneEquals(except, exceptClass, actual, actualClass, instance);
        }
    }

    /**
     * 是否对某个类起用字段的递归比较
     *
     * @param target 类型
     * @return true: 启用; false: 不启用
     */
    protected boolean isEnableRecursiveCompare(final Class<?> target) {
        // 基本类型或包装类型，无需递归
        if (ClassUtils.isPrimitiveOrWrapper(target)) {
            return false;
        }

        // 枚举类型，无需递归
        if (Enum.class.isAssignableFrom(target)) {
            return false;
        }

        // 集合复杂类型，需要递归
        if (GatherEnum.delegate(target).isPresent()) {
            return true;
        }

        // 其他 jdk 原生对象，无需递归
        if (target.getPackage().getName().startsWith("java.")) {
            return false;
        }

        return true;
    }

    protected void doOneEquals(Object except, Class<?> exceptClass, Object actual, Class<?> actualClass, VerifyInstance instance) {
        if (exceptClass == actualClass) {
            this.doOneEqualsWithSameClass(except, actual, exceptClass, instance);
        } else {
            switch (instance.getClassDiffVerifyStrategy()) {
                case CONVERT_SAME_CLASS:
                    final Object newActual = ProviderFactory.delegate(ConvertProvider.class).convert(actual, exceptClass);
                    this.doOneEqualsWithSameClass(except, newActual, exceptClass, instance);
                    break;
                case VERIFY_SAME_FIELD:
                    this.doOneEqualsWithDifferentClass(except, exceptClass, actual, actualClass, instance);
                    break;
            }
        }
    }

    /**
     * 单组相同类型的对象比对
     *
     * @param except   except object
     * @param actual   actual object
     * @param target   the object class
     * @param instance 上下文属性
     * @throws AssertionError if {@code actual} is not equal to {@code expected}. This method will throw a
     *                        {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the given objects are not
     *                        equal.
     */
    private void doOneEqualsWithSameClass(Object except, Object actual, Class<?> target, VerifyInstance instance) {
        if (isEnableRecursiveCompare(target)) {

            final ClassGroupEnum classGroup = ClassGroupEnum.delegate(target);

            // proto 对象比较特殊，不建议使用 assertj 原生的比较，因为比较的字段是含 _ 的
            if (classGroup.getGroup() == ClassGroupEnum.Group.PROTOBUF) {
                final Set<String> doValidFields = this.initialValidFields(e -> PropertyCache.tryGet(target).getReadable().keySet(), instance);

                this.doVerifyByEachField(doValidFields, except, target, actual, target, instance);
            } else {
                RecursiveComparisonAssert<?> anAssert = assertThat(actual).usingRecursiveComparison();

                final Set<String> ignoreFields = instance.getIgnoreFields();
                if (CollectionUtils.isNotEmpty(ignoreFields)) {
                    anAssert = anAssert.ignoringFields(ignoreFields.toArray(new String[0]));
                }

                final Set<String> validateFields = instance.getValidateFields();
                if (CollectionUtils.isNotEmpty(validateFields)) {
                    anAssert = anAssert.comparingOnlyFields(validateFields.toArray(new String[0]));
                }

                anAssert = EasyVerifyComp.usingComparatorForType(anAssert, instance.getPrecisionConfigs());

                anAssert.isEqualTo(except);
            }
        } else {
            EasyVerifyComp
                    .usingComparator(assertThat(actual), target, instance.getPrecisionConfigs())
                    .isEqualTo(except);
        }
    }

    /**
     * 单组相同类型的对象比对
     *
     * @param except      except object
     * @param actual      actual object
     * @param exceptClass the except object class
     * @param actualClass the actual object class
     * @param instance    上下文属性
     * @throws AssertionError if {@code actual} is not equal to {@code expected}. This method will throw a
     *                        {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the given objects are not
     *                        equal.
     */
    private void doOneEqualsWithDifferentClass(Object except, Class<?> exceptClass, Object actual, Class<?> actualClass, VerifyInstance instance) {
        final boolean aEnableRecursiveCompare = isEnableRecursiveCompare(exceptClass), bEnableRecursiveCompare = isEnableRecursiveCompare(actualClass);

        // 一个需要递归，一个不需要递归
        if (aEnableRecursiveCompare ^ bEnableRecursiveCompare) {
            fail("Verify Failed, One Not Recursive Compare And One Recursive Compare");
        }

        if (aEnableRecursiveCompare) {
            final Set<String> doValidFields = this.initialValidFields(e -> {
                final Set<String> aSet = PropertyCache.tryGet(exceptClass).getReadable().keySet();
                final Set<String> bSet = PropertyCache.tryGet(actualClass).getReadable().keySet();

                return CollectionUtils.intersection(aSet, bSet);
            }, instance);

            this.doVerifyByEachField(doValidFields, except, exceptClass, actual, actualClass, instance);
        } else {
            final Object newActual = ProviderFactory.delegate(ConvertProvider.class).convert(actual, exceptClass);

            EasyVerifyComp
                    .usingComparator(assertThat(newActual), exceptClass, instance.getPrecisionConfigs())
                    .isEqualTo(except);
        }
    }

    /**
     * 初始化需要比对的字段列表
     * <p>
     * 1.1 有指定比较字段，使用指定字段
     * 1.2 如果没有指定，使用对象字段交集
     * 2. 有指定忽略字段，将其排除
     *
     * @param instance          {@link VerifyInstance}
     * @param pendingFieldsFunc 当没有指定比较字段时，需要提供待处理的字段列表
     * @return 需要比对的字段列表
     */
    private Set<String> initialValidFields(Function<Object, Collection<String>> pendingFieldsFunc, VerifyInstance instance) {
        final HashSet<String> doValidFields = Sets.newHashSet(instance.getValidateFields());

        if (CollectionUtils.isEmpty(doValidFields)) {
            final Collection<String> pendingFields = pendingFieldsFunc.apply(null);

            if (CollectionUtils.isNotEmpty(pendingFields)) {
                doValidFields.addAll(pendingFields);
            }
        }

        doValidFields.removeAll(Sets.newHashSet(instance.getIgnoreFields()));

        return doValidFields;
    }

    private void doVerifyByEachField(final Set<String> validFields, Object except, Class<?> exceptClass, Object actual, Class<?> actualClass, VerifyInstance instance) {
        for (String fieldName : validFields) {
            try {
                final FieldProperty exceptProperty = PropertyCache.tryGet(exceptClass, fieldName), actualProperty = PropertyCache.tryGet(actualClass, fieldName);

                final Class<?> propertyClass = exceptProperty.getTarget();
                final Object exceptValue = exceptProperty.getReadFunc().apply(except);

                Object actualValue = actualProperty.getReadFunc().apply(actual);
                if (actualProperty.getTarget() != propertyClass) {
                    actualValue = ProviderFactory.delegate(ConvertProvider.class).convert(actualValue, propertyClass);
                }

                EasyVerifyComp
                        .usingComparator(assertThat(actualValue), propertyClass, instance.getPrecisionConfigs())
                        .as("fieldName: %s", fieldName)
                        .isEqualTo(exceptValue);
            } catch (AssertionError assertionError) {
                throw assertionError;
            } catch (Throwable throwable) {
                throw new EasyDataVerifyException(throwable);
            }
        }
    }
}
