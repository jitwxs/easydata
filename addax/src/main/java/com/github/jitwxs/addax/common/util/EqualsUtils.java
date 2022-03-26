package com.github.jitwxs.addax.common.util;

import com.google.protobuf.Message;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 22:19
 */
public class EqualsUtils {
    /**
     * 异或运算，要么全部满足，要么全部不满足
     */
    private static final BiConsumer<Pair<Class<?>, Class<?>>, Function<Class<?>, Boolean>> classAssert = (pair, judge) -> {
        if (judge.apply(pair.getLeft()) ^ judge.apply(pair.getRight())) {
            fail("Class Check Failed");
        }
    };

    /**
     * 判断是否其中一个是 proto 类
     */
    public static boolean hasOneProto(final Pair<Class<?>, Class<?>> classPair) {
        try {
            classAssert.accept(classPair, Message.class::isAssignableFrom);
            return false;
        } catch (AssertionError e) {
            return true;
        }
    }

//    /**
//     * 是否是（广义）集合类型
//     */
//    public static GatherEnum isGather(final Class<?> clazz) {
//        if (clazz.isArray()) {
//            return GatherEnum.ARRAY;
//        }
//
//        if (Map.class.isAssignableFrom(clazz)) {
//
//        }
//
//        return clazz.isArray() ||  || Collection.class.isAssignableFrom(clazz);
//    }
}
