package com.github.jitwxs.addax.core.verify;

import com.github.jitwxs.addax.core.verify.impl.BeanTypeVerify;
import com.github.jitwxs.addax.core.verify.impl.CollectionTypeVerify;
import com.github.jitwxs.addax.core.verify.impl.MapTypeVerify;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 21:41
 */
@Getter
public class Verify {
    public final static BeanTypeVerify BEAN_TYPE_VERIFY = new BeanTypeVerify();

    public final static MapTypeVerify MAP_TYPE_VERIFY = new MapTypeVerify();

    public final static CollectionTypeVerify COLLECTION_TYPE_VERIFY = new CollectionTypeVerify();

    private final VerifyContext context = new VerifyContext();

    private final Object except;

    private Object actual;

    public Verify(@NonNull final Object except) {
        this.except = except;
    }

    /**
     * 忽略比较字段
     */
    public Verify ignoredFields(final String... fields) {
        if (fields != null && fields.length > 0) {
            context.getIgnoreFields().addAll(Arrays.asList(fields));
        }

        return this;
    }

    /**
     * 指定比较字段
     */
    public Verify validateFields(final String... fields) {
        if (fields != null && fields.length > 0) {
            context.getValidateFields().addAll(Arrays.asList(fields));
        }

        return this;
    }

    /**
     * 忽略不同类型直接的差异
     *
     * 例如 set -> list, hashmap -> treemap
     */
    public Verify ignoreClassDiff() {
        this.context.setIgnoreClassDiff(true);

        return this;
    }

    public void asserts(@NonNull final Object actual) {
        this.actual = actual;

        BEAN_TYPE_VERIFY.check(except, actual, context);
    }
}
