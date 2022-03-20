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
     * 随机长度的英语字符
     */
    ENGLISH,
    /**
     * 随机长度的数字
     */
    NUMBER,
    /**
     * 邮箱
     */
    EMAIL,
    /**
     * 中国身份证号
     */
    CN_ID_CARD,
    /**
     * 中国手机号码
     */
    CN_TEL,
    /**
     * 中文名
     */
    CN_NAME,
    /**
     * 英文名
     */
    ENGLISH_NAME,
}
