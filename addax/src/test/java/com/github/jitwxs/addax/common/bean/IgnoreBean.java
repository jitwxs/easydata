package com.github.jitwxs.addax.common.bean;

import com.github.jitwxs.addax.common.annotation.AddaxMockIgnore;
import lombok.Data;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 19:35
 */
@Data
public class IgnoreBean {
    @AddaxMockIgnore
    private String ignore;

    private Integer notIgnore;
}
