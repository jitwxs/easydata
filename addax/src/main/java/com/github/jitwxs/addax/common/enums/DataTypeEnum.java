package com.github.jitwxs.addax.common.enums;

import com.github.jitwxs.addax.common.exception.AddaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:43
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum {
    /**
     * JSON 类型数据
     */
    JSON,
    /**
     * 矩阵类型数据
     */
    MATRIX
    ;

    public static DataTypeEnum delegate(FileFormatEnum fileFormatEnum) {
        switch (fileFormatEnum) {
            case JSON:
                return JSON;
            case CSV:
                return MATRIX;
            default:
                throw new AddaxException(String.format("Not Support FileFormatEnum(%s) To Convert DataTypeEnum", fileFormatEnum));
        }
    }
}
