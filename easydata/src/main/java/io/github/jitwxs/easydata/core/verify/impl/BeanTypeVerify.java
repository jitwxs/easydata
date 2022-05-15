package io.github.jitwxs.easydata.core.verify.impl;

import com.google.common.collect.Sets;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
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

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static io.github.jitwxs.easydata.core.verify.EasyVerify.COLLECTION_TYPE_VERIFY;
import static io.github.jitwxs.easydata.core.verify.EasyVerify.MAP_TYPE_VERIFY;
import static org.assertj.core.api.Assertions.*;

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

        // 其他 java 原生对象，无需递归
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
            /*
             * 计算需要比较的字段
             * 1.1 上下文有指定比较字段，使用指定字段
             * 1.2 如果没有指定，使用对象字段交集
             * 2. 上下文有指定忽略字段，将其排除
             */
            final Set<String> doValidFields = Sets.newHashSet(instance.toValidateFields());

            if (CollectionUtils.isEmpty(doValidFields)) {
                final Set<String> aSet = PropertyCache.tryGet(aClass).getReadable().keySet();
                final Set<String> bSet = PropertyCache.tryGet(bClass).getReadable().keySet();

                doValidFields.addAll(CollectionUtils.intersection(aSet, bSet));
            }

            doValidFields.removeAll(Sets.newHashSet(instance.toIgnoreFields()));

            if (CollectionUtils.isEmpty(doValidFields)) {
                return;
            }

            /*
             * 逐个字段调用比较方法
             */
            for (String fieldName : doValidFields) {
                try {
                    final PropertyDescriptor aDescriptor = PropertyCache.tryGet(aClass).getDescriptor(fieldName);
                    final PropertyDescriptor bDescriptor = PropertyCache.tryGet(bClass).getDescriptor(fieldName);

                    final Class<?> propertyType = aDescriptor.getPropertyType();
                    final Object aValue = aDescriptor.getReadMethod().invoke(a);

                    Object bValue = bDescriptor.getReadMethod().invoke(b);
                    if (bDescriptor.getPropertyType() != propertyType) {
                        bValue = ProviderFactory.delegate(ConvertProvider.class).convert(bValue, propertyType);
                    }

                    ObjectAssert<Object> anAssert = assertThat(aValue);

                    final BaseComp comp = instance.getCompConfigs().get(propertyType);
                    if (comp != null) {
                        anAssert = anAssert.usingComparator(comp);
                    }

                    anAssert.isEqualTo(bValue);

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new EasyVerifyEqualsException(e);
                }
            }
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
}
