package io.github.jitwxs.easydata.sample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 *
 * @since 2019/4/16 23:51
 */
@Getter
@AllArgsConstructor
public enum ResponseErrorCodeEnum {
    /* SUCCESS */
    SUCCESS(0, 200, "成功"),

    /* Bad Request */
    PARAMETER_ERROR(400001, 200, "参数错误"),
    SYSTEM_ERROR(400002, 200, "系统错误"),
    ;

    private final int code;

    private final int status;

    private final String msg;
}
