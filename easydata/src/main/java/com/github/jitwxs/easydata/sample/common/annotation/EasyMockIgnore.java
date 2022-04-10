package com.github.jitwxs.easydata.sample.common.annotation;

import java.lang.annotation.*;

/**
 * mock 时忽略字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyMockIgnore {

}
