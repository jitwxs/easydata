package com.github.jitwxs.addax.core.loader;

import com.github.jitwxs.addax.common.bean.LoaderPropertiesUrl;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:44
 */
public interface LoaderProperties {
    /**
     * 加载类
     */
    Class<?> clazz();

    /**
     * 查询路径
     */
    LoaderPropertiesUrl url();

    /**
     * 额外指定的字段映射
     */
    List<Pair<String, String>> extraFieldMappings();
}
