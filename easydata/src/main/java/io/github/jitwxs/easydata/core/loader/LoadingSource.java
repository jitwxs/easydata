package io.github.jitwxs.easydata.core.loader;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.DataTypeEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataLoaderException;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 12:02
 */
@Getter
public abstract class LoadingSource<T> {
    protected final DataTypeEnum dataType;

    protected final LoaderProperties properties;

    protected final T source;

    public LoadingSource(DataTypeEnum dataType, LoaderProperties properties, T source) {
        this.dataType = dataType;
        this.properties = properties;
        this.source = source;
    }

    public abstract <K> List<K> toBean(final Class<K> target, final LoaderProperties properties);

    protected BiMap<String, String> initialExtraFields(final LoaderProperties properties) {
        final BiMap<String, String> extraFiledMap = HashBiMap.create();

        final String[] fields = properties.getExtraFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i += 2) {
                extraFiledMap.put(fields[i], fields[i + 1]);
            }
        }

        return extraFiledMap;
    }

    /**
     * 填充字段
     *
     * @param object     对象
     * @param fieldValue 字段值
     * @param fieldName  字段名
     */
    public void fillingField(final Object object, final Object fieldValue, final String[] fieldName) {
        if (fieldName == null) {
            return;
        }

        final Class<?> target = object.getClass();

        final ConvertProvider provider = ProviderFactory.delegate(ConvertProvider.class);

        for (String name : fieldName) {
            try {
                if (StringUtils.isBlank(name)) {
                    continue;
                }

                final FieldProperty property = PropertyCache.tryGet(target, name);

                if (property != null && property.isWriteable()) {
                    final Object value = provider.convert(fieldValue, property.getTarget());

                    property.getWriteFunc().apply(object, value);
                }
            } catch (Throwable e) {
                throw new EasyDataLoaderException(e);
            }
        }
    }
}
