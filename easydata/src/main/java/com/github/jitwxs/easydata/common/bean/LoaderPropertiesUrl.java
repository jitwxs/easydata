package com.github.jitwxs.easydata.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 23:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoaderPropertiesUrl {
    private String fileConn;

    private String sqlConn;
}
