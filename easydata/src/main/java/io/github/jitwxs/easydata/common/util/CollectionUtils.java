package io.github.jitwxs.easydata.common.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-26 22:59
 */
public class CollectionUtils {
    public static <T> T randomElement(Collection<T> c) {
        int random = (int) (Math.random() * c.size());
        Iterator<T> it = c.iterator();
        for (int i = 0; i < random; i++) {
            it.next();
        }
        return it.next();
    }
}
