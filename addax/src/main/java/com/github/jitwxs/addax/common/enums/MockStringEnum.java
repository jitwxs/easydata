package com.github.jitwxs.addax.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:56
 */
@Getter
@AllArgsConstructor
public enum MockStringEnum {
    /**
     * uuid
     */
    UUID,
    /**
     * 随机长度的字符
     */
    CHARACTER,
    /**
     * 随机长度的数字
     */
    NUMBER,
}
