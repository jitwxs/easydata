package io.github.jitwxs.easydata.core.loader;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

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
     * 加载路径
     */
    private String url;

    /**
     * 额外指定的字段映射
     */
    private String[] extraFields;

    /**
     * 指定 JSON 类型反序列化策略
     */
    private Function<String, ?> jsonDeserializeFunc;

    public static LoaderPropertiesBuilder builder() {
        return new LoaderPropertiesSelfBuilder();
    }

    // just support javadoc compile
    public static class LoaderPropertiesBuilder {
    }

    static class LoaderPropertiesSelfBuilder extends LoaderPropertiesBuilder {
        @Override
        public LoaderProperties build() {
            final LoaderProperties properties = super.build();

            if (StringUtils.isBlank(properties.getUrl())) {
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
