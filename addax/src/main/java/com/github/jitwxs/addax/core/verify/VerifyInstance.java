package com.github.jitwxs.addax.core.verify;

import com.github.jitwxs.addax.common.util.ObjectUtils;
import com.github.jitwxs.addax.common.util.ReflectionUtils;
import com.github.jitwxs.addax.core.verify.comp.BaseComp;
import lombok.Getter;
import lombok.NonNull;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;

import static com.github.jitwxs.addax.core.verify.Verify.BEAN_TYPE_VERIFY;
import static java.util.Collections.singletonList;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-27 22:16
 */
public class VerifyInstance {
    private final Object except;

    private final Object actual;

    private final Set<String> ignoreFields = new HashSet<>();

    private final Set<String> validateFields = new HashSet<>();

    private final Map<Class<?>, Object> precisionConfigs = new HashMap<>();

    @Getter
    private Map<Class<?>, BaseComp> compConfigs;

    /**
     * 忽略类型的差异
     */
    @Getter
    private boolean ignoreClassDiff = false;

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
     * 忽略不同类型直接的差异
     * <p>
     * 例如 {@link Set} 和 {@link List}，{@link HashMap} 和 {@link TreeMap} 等
     *
     * @return this
     */
    public VerifyInstance ignoreClassDiff() {
        this.ignoreClassDiff = true;
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
    public void run() {
        this.collect();

        BEAN_TYPE_VERIFY.check(except, actual, this);
    }

    /**
     * 将忽略字段转换为 string 数组结构
     *
     * @return 忽略字段列表
     */
    public String[] toIgnoreFields() {
        return ignoreFields.toArray(new String[0]);
    }

    /**
     * 将比较的字段转换为 string 数组结构
     *
     * @return 比较字段列表
     */
    public String[] toValidateFields() {
        return validateFields.toArray(new String[0]);
    }

    private void collect() {
        // 初始化 compConfigs
        compConfigs = new HashMap<>();
        new Reflections(BaseComp.class.getPackage().getName()).getSubTypesOf(BaseComp.class).forEach(c -> {
            if (Modifier.isInterface(c.getModifiers())) {
                return;
            }

            final Class<?> targetType = (Class<?>) ReflectionUtils.getGenericSuperClass(c)[0];

            final BaseComp comp;
            if (precisionConfigs.containsKey(targetType)) {
                comp = ObjectUtils.create(c, singletonList(targetType), singletonList(precisionConfigs.get(targetType)));
            } else {
                comp = ObjectUtils.create(c);
            }

            compConfigs.put(targetType, comp);
        });
    }
}
