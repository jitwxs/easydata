package io.github.jitwxs.easydata.common.cache;

import com.google.protobuf.ProtocolMessageEnum;
import lombok.Getter;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 13:16
 */
public class EnumCache {
    private static final Function<Enum<?>, Integer> DEFAULT_GET_ID_FUNC = Enum::ordinal;

    private static final Map<Class<?>, EnumProperty> cache = new HashMap<>();

    public static EnumProperty tryGet(final Class<?> target) {
        return tryGet(target, DEFAULT_GET_ID_FUNC);
    }

    public static EnumProperty tryGet(final Class<?> target, final Function<Enum<?>, Integer> getIdFunc) {
        if (!target.isEnum()) {
            return null;
        }

        return cache.computeIfAbsent(target, i -> new EnumProperty(target, getIdFunc));
    }

    @Getter
    public static class EnumProperty {
        private final Class<?> target;

        private final boolean isProto;

        private final Map<String, Enum> nameMap = new HashMap<>();

        private final Map<Integer, Enum> idMap = new HashMap<>();

        public EnumProperty(final Class<?> target, final Function<Enum<?>, Integer> getIdFunc) {
            this.target = target;
            this.isProto = ProtocolMessageEnum.class.isAssignableFrom(target);

            final Enum<?>[] enums = Whitebox.getInternalState(target, "$VALUES");

            for (Enum<?> one : enums) {
                if (isProto) {
                    if ("UNRECOGNIZED".equals(one.name())) {
                        continue;
                    }
                    this.idMap.put(((ProtocolMessageEnum) one).getNumber(), one);
                } else {
                    this.idMap.put(getIdFunc.apply(one), one);
                }

                this.nameMap.put(one.name(), one);
            }
        }
    }
}
