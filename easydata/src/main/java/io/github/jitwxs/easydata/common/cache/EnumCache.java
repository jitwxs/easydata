package io.github.jitwxs.easydata.common.cache;

import com.google.protobuf.ProtocolMessageEnum;
import io.github.jitwxs.easydata.common.util.CollectionUtils;
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

    public static class EnumProperty {
        @Getter
        private final Class<?> target;

        @Getter
        private final boolean isProto;

        private final Map<String, Enum> nameMap = new HashMap<>();

        @Getter
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

                this.nameMap.put(one.name().toUpperCase(), one);
            }
        }

        public Enum getByName(final String name) {
            return this.nameMap.get(name.toUpperCase());
        }

        public Enum random() {
            return CollectionUtils.randomElement(this.idMap.values());
        }
    }
}
