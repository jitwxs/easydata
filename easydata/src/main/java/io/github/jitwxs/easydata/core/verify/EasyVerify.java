package io.github.jitwxs.easydata.core.verify;

import io.github.jitwxs.easydata.core.verify.impl.BeanTypeVerify;
import io.github.jitwxs.easydata.core.verify.impl.CollectionTypeVerify;
import io.github.jitwxs.easydata.core.verify.impl.MapTypeVerify;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 21:41
 */
@Getter
public class EasyVerify {
    public final static BeanTypeVerify BEAN_TYPE_VERIFY = new BeanTypeVerify();

    public final static MapTypeVerify MAP_TYPE_VERIFY = new MapTypeVerify();

    public final static CollectionTypeVerify COLLECTION_TYPE_VERIFY = new CollectionTypeVerify();

    public static VerifyInstance with(@NonNull final Object except, @NonNull final Object actual) {
        return new VerifyInstance(except, actual);
    }
}
