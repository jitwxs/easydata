package io.github.jitwxs.easydata.common.enums;

/**
 * 不同类型的类对象比较策略
 *
 * @author jitwxs@foxmail.com
 * @since 2022-05-15 14:30
 */
public enum ClassDiffVerifyStrategyEnum {
    /**
     * 使用内置的转换逻辑，将两个不同的类，转换成相同的类，再进行比较
     */
    CONVERT_SAME_CLASS,

    /**
     * 比较两个类都有的字段，其他字段不比较
     */
    VERIFY_SAME_FIELD,
    ;
}
