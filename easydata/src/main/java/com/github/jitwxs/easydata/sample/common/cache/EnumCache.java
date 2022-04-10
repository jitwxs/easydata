package com.github.jitwxs.easydata.sample.common.cache;

import com.google.protobuf.ProtocolMessageEnum;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 13:16
 */
public class EnumCache {
    private static final Map<Class<?>, Enum<?>[]> cache = new HashMap<>();

    public static Enum<?>[] tryGet(final Class<?> clazz) {
        return cache.computeIfAbsent(clazz, i -> {
            if (!clazz.isEnum()) {
                return null;
            }

            Enum<?>[] enums = Whitebox.getInternalState(clazz, "$VALUES");

            // 对 proto 支持，忽略无效属性
            if (ProtocolMessageEnum.class.isAssignableFrom(clazz)) {
                final Enum<?>[] protoEnums = new Enum<?>[enums.length - 1];
                int k = 0;

                for (Enum<?> one : enums) {
                    if ("UNRECOGNIZED".equals(one.name())) {
                        continue;
                    }

                    protoEnums[k++] = one;
                }

                enums = protoEnums;
            }

            return enums;
        });
    }
}
