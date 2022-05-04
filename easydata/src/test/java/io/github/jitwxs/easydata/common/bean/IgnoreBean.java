package io.github.jitwxs.easydata.common.bean;

import io.github.jitwxs.easydata.common.annotation.EasyMockIgnore;
import lombok.Data;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 19:35
 */
@Data
public class IgnoreBean {
    @EasyMockIgnore
    private String ignore;

    private Integer notIgnore;
}
