package io.github.jitwxs.easydata.core.verify.impl;

import com.google.common.collect.Sets;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.ClassGroupEnum;
import io.github.jitwxs.easydata.common.enums.GatherEnum;
import io.github.jitwxs.easydata.common.exception.EasyVerifyEqualsException;
import io.github.jitwxs.easydata.core.verify.VerifyInstance;
import io.github.jitwxs.easydata.core.verify.comp.BaseComp;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.RecursiveComparisonAssert;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    public void check(Object a, Object b, VerifyInstance instance) {
        final Class<?> aClass = a.getClass(), bClass = b.getClass();

        // 类型强校验
        if (instance.getClassDiffVerifyStrategy() == null) {
            assertThat(a).hasSameClassAs(b);
        }

        final Optional<GatherEnum> gatherA = GatherEnum.delegate(aClass);
        final Optional<GatherEnum> gatherB = GatherEnum.delegate(bClass);

        // 一个复杂类型、一个非复杂类型
        if (gatherA.isPresent() ^ gatherB.isPresent()) {
            fail("Verify Failed, One Simple Class And One Gather Class");
        }

        if (gatherA.isPresent() && gatherB.isPresent()) {
            // 两个都是复杂类型
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
            // 两个都是非复杂类型
            this.doOneEquals(a, aClass, b, bClass, instance);
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

    protected void doOneEquals(Object a, Class<?> aClass, Object b, Class<?> bClass, VerifyInstance instance) {
        if (aClass == bClass) {
            this.doOneEqualsWithSameClass(a, b, aClass, instance);
        } else {
            switch (instance.getClassDiffVerifyStrategy()) {
                case CONVERT_SAME_CLASS:
                    final Object bValue = ProviderFactory.delegate(ConvertProvider.class).convert(b, aClass);
                    this.doOneEqualsWithSameClass(a, bValue, aClass, instance);
                    break;
                case VERIFY_SAME_FIELD:
                    this.doOneEqualsWithDifferentClass(a, aClass, b, bClass, instance);
                    break;
            }
        }
    }

    /**
     * 单组相同类型的对象比对
     *
     * @param a        对象 A
     * @param b        对象 B
     * @param target   类型
     * @param instance 上下文属性
     * @throws AssertionError if {@code actual} is not equal to {@code expected}. This method will throw a
     *                        {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the given objects are not
     *                        equal.
     */
    private void doOneEqualsWithSameClass(Object a, Object b, Class<?> target, VerifyInstance instance) {
        if (isEnableRecursiveCompare(target)) {

            final ClassGroupEnum classGroup = ClassGroupEnum.delegate(target);

            // proto 对象比较特殊，不建议使用 assertj 原生的比较，因为比较的字段是含 _ 的
            if (classGroup.getGroup() == ClassGroupEnum.Group.PROTOBUF) {
                final Set<String> doValidFields = this.initialValidFields(e -> PropertyCache.tryGet(target).getReadable().keySet(), instance);

                this.doVerifyByEachField(doValidFields, a, target, b, target, instance);
            } else {
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
            }
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
     * 单组相同类型的对象比对
     *
     * @param a        对象 A
     * @param b        对象 B
     * @param aClass   对象 A 类型
     * @param bClass   对象 B 类型
     * @param instance 上下文属性
     * @throws AssertionError if {@code actual} is not equal to {@code expected}. This method will throw a
     *                        {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the given objects are not
     *                        equal.
     */
    private void doOneEqualsWithDifferentClass(Object a, Class<?> aClass, Object b, Class<?> bClass, VerifyInstance instance) {
        final boolean aEnableRecursiveCompare = isEnableRecursiveCompare(aClass), bEnableRecursiveCompare = isEnableRecursiveCompare(bClass);

        // 一个需要递归，一个不需要递归
        if (aEnableRecursiveCompare ^ bEnableRecursiveCompare) {
            fail("Verify Failed, One Not Recursive Compare And One Recursive Compare");
        }

        if (aEnableRecursiveCompare) {
            final Set<String> doValidFields = this.initialValidFields(e -> {
                final Set<String> aSet = PropertyCache.tryGet(aClass).getReadable().keySet();
                final Set<String> bSet = PropertyCache.tryGet(bClass).getReadable().keySet();

                return CollectionUtils.intersection(aSet, bSet);
            }, instance);

            this.doVerifyByEachField(doValidFields, a, aClass, b, bClass, instance);
        } else {
            ObjectAssert<Object> anAssert = assertThat(a);

            final Object bValue = ProviderFactory.delegate(ConvertProvider.class).convert(b, aClass);

            final BaseComp comp = instance.getCompConfigs().get(aClass);
            if (comp != null) {
                anAssert = anAssert.usingComparator(comp);
            }

            anAssert.isEqualTo(bValue);
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
        final Set<String> doValidFields = Sets.newHashSet(instance.toValidateFields());

        if (CollectionUtils.isEmpty(doValidFields)) {
            final Collection<String> pendingFields = pendingFieldsFunc.apply(null);

            if (CollectionUtils.isNotEmpty(pendingFields)) {
                doValidFields.addAll(pendingFields);
            }
        }

        doValidFields.removeAll(Sets.newHashSet(instance.toIgnoreFields()));

        return doValidFields;
    }

    private void doVerifyByEachField(final Set<String> validFields, Object a, Class<?> aClass, Object b, Class<?> bClass, VerifyInstance instance) {
        for (String fieldName : validFields) {
            try {
                final FieldProperty aProperty = PropertyCache.tryGet(aClass, fieldName);
                final FieldProperty bProperty = PropertyCache.tryGet(bClass, fieldName);

                final Class<?> propertyClass = aProperty.getTarget();
                final Object aValue = aProperty.getReadFunc().apply(a);

                Object bValue = bProperty.getReadFunc().apply(b);
                if (bProperty.getTarget() != propertyClass) {
                    bValue = ProviderFactory.delegate(ConvertProvider.class).convert(bValue, propertyClass);
                }

                ObjectAssert<Object> anAssert = assertThat(aValue);

                final BaseComp comp = instance.getCompConfigs().get(propertyClass);
                if (comp != null) {
                    anAssert = anAssert.usingComparator(comp);
                }

                anAssert
                        .as("fieldName: %s", fieldName)
                        .isEqualTo(bValue);

            } catch (AssertionError assertionError) {
                throw assertionError;
            } catch (Throwable throwable) {
                throw new EasyVerifyEqualsException(throwable);
            }
        }
    }
}
