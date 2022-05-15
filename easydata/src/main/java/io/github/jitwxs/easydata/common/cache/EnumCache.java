package io.github.jitwxs.easydata.common.cache;

import com.google.protobuf.ProtocolMessageEnum;
import lombok.Getter;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 13:16
 */
public class EnumCache {
    private static final Map<Class<?>, EnumProperty> cache = new HashMap<>();

    public static EnumProperty tryGet(final Class<?> target) {
        if (!target.isEnum()) {
            return null;
        }

        return cache.computeIfAbsent(target, i -> new EnumProperty(target));
    }

    public static Enum tryGet(final Class<?> target, final String name, final int faultId) {
        final EnumProperty property = tryGet(target);
        if (property == null) {
            return null;
        }

        return property.getNameMap().getOrDefault(name, property.getIdMap().get(faultId));
    }

    @Getter
    public static class EnumProperty {
        private final Class<?> target;

        private final boolean isProto;

        private final Map<String, Enum> nameMap = new HashMap<>();

        private final Map<Integer, Enum> idMap = new HashMap<>();

        public EnumProperty(Class<?> target) {
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
                    this.idMap.put(one.ordinal(), one);
                }

                this.nameMap.put(one.name(), one);
            }
        }
    }
}
