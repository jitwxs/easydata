package io.github.jitwxs.easydata.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 18:55
 */
@Getter
@AllArgsConstructor
public enum GatherEnum {
    /**
     * 集合类型
     */
    COLLECTION(Collection.class::isAssignableFrom),

    /**
     * map 类型
     */
    MAP(Map.class::isAssignableFrom),

    /**
     * 数组类型
     */
    ARRAY(Class::isArray)
    ;

    /**
     * 判断表达式
     */
    private final Function<Class<?>, Boolean> judgeFuc;

    public static Optional<GatherEnum> delegate(final Class<?> clazz) {
        for (GatherEnum gatherEnum : values()) {
            if (gatherEnum.judgeFuc.apply(clazz)) {
                return Optional.of(gatherEnum);
            }
        }

        return Optional.empty();
    }
}
