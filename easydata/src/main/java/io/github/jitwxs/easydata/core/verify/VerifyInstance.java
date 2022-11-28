package io.github.jitwxs.easydata.core.verify;

import io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 22:16
 */
@Getter
public class VerifyInstance implements Cloneable {
    private final Object except;

    private final Object actual;

    private final Set<String> ignoreFields = new HashSet<>();

    private final Set<Object> ignoreElements = new HashSet<>();

    private final Set<String> validateFields = new HashSet<>();

    private final Map<Class<?>, Object> precisionConfigs = new HashMap<>();

    /**
     * 忽略类型的差异
     */
    private ClassDiffVerifyStrategyEnum classDiffVerifyStrategy = null;

    public VerifyInstance(Object except, Object actual) {
        this.except = except;
        this.actual = actual;
    }

    /**
     * 忽略比较字段
     *
     * @param fields 忽略字段列表
     * @return this
     */
    public VerifyInstance ignoredFields(final String... fields) {
        if (fields != null && fields.length > 0) {
            this.ignoreFields.addAll(Arrays.asList(fields));
        }
        return this;
    }

    /**
     * 忽略比较元素
     *
     * @param elements 忽略元素列表
     * @return this
     */
    public VerifyInstance ignoreElements(final String... elements) {
        if (elements != null && elements.length > 0) {
            this.ignoreElements.addAll(Arrays.asList(elements));
        }
        return this;
    }

    /**
     * 指定比较字段
     *
     * @param fields 比较字段列表
     * @return this
     */
    public VerifyInstance validateFields(final String... fields) {
        if (fields != null && fields.length > 0) {
            this.validateFields.addAll(Arrays.asList(fields));
        }
        return this;
    }

    /**
     * 忽略不同类型之间的差异
     * <p>
     * 例如 {@link Set} 和 {@link List}，{@link HashMap} 和 {@link TreeMap} 等
     *
     * @param classDiffVerifyStrategy 比较策略，传 null 时表示不忽略类型差异
     * @return this
     */
    public VerifyInstance ignoreClassDiff(ClassDiffVerifyStrategyEnum classDiffVerifyStrategy) {
        this.classDiffVerifyStrategy = classDiffVerifyStrategy;
        return this;
    }

    /**
     * 配置精度误差值
     *
     * @param precision 精度误差配置
     * @return this
     */
    public VerifyInstance withPrecision(@NonNull Object precision) {
        this.precisionConfigs.put(precision.getClass(), precision);
        return this;
    }

    /**
     * 真正执行 verify 的方法
     */
    public void verify() {
        EasyVerify.BEAN_TYPE_VERIFY.check(except, actual, this);
    }

    @Override
    public VerifyInstance clone() {
        try {
            VerifyInstance clone = (VerifyInstance) super.clone();
            // copy mutable state here, so the clone can't change the internals of the original

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
