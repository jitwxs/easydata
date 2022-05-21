package io.github.jitwxs.easydata.common.enums;

import com.google.protobuf.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-21 17:38
 */
@Getter
@AllArgsConstructor
public enum ClassGroupEnum {
    /**
     * protobuf message 类型
     */
    PROTOBUF_MESSAGE(ClassGroupEnum.Group.PROTOBUF),
    /**
     * protobuf message 类型
     */
    PROTOBUF_BUILDER(ClassGroupEnum.Group.PROTOBUF),
    /**
     * 原生类型
     */
    NATIVE(ClassGroupEnum.Group.NATIVE);

    private final Group group;
    
    public enum Group {
        PROTOBUF, NATIVE
    }

    public static ClassGroupEnum delegate(final Class<?> target) {
        if (Message.class.isAssignableFrom(target)) {
            return PROTOBUF_MESSAGE;
        } else if (Message.Builder.class.isAssignableFrom(target)) {
            return PROTOBUF_BUILDER;
        } else {
            return NATIVE;
        }
    }
}
