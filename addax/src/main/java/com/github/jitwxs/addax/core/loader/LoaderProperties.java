package com.github.jitwxs.addax.core.loader;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:44
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoaderProperties {
    /**
     * 加载类
     */
    private Class<?> clazz;

    private String fileUrl;

    private String sqlUrl;

    /**
     * 额外指定的字段映射
     */
    private String[] extraFields;

    public static LoaderPropertiesBuilder builder() {
        return new LoaderPropertiesSelfBuilder();
    }

    static class LoaderPropertiesSelfBuilder extends LoaderPropertiesBuilder {
        @Override
        public LoaderProperties build() {
            final LoaderProperties properties = super.build();

            if (properties.getClazz() == null) {
                throw new IllegalArgumentException("params clazz must define");
            }

            if (StringUtils.isAllBlank(properties.getSqlUrl(), properties.getFileUrl())) {
                throw new IllegalArgumentException("params url must define");
            }

            final String[] extraFields = properties.getExtraFields();
            if (extraFields != null && extraFields.length % 2 != 0) {
                throw new IllegalArgumentException("params extraFields must configuration in pairs");
            }

            return properties;
        }
    }
}
