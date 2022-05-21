package io.github.jitwxs.easydata.common.enums;

import com.google.protobuf.Message;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-21 17:38
 */
public enum ClassGroupEnum {
    /**
     * protobuf 类型
     */
    PROTOBUF,
    /**
     * 原生类型
     */
    NATIVE
    ;

    public static ClassGroupEnum delegate(final Class<?> target) {
        if (Message.class.isAssignableFrom(target)) {
            return PROTOBUF;
        } else {
            return NATIVE;
        }
    }
}
