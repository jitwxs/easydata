package io.github.jitwxs.easydata.core.loader;

import com.google.common.collect.BiMap;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.DataTypeEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.powermock.reflect.Whitebox;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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

    public abstract <K> List<K> toBean(final Class<K> target, final BiMap<String, String> extraFiledMap);

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
        final Map<String, PropertyDescriptor> descriptorsMap = PropertyCache.tryGet(target).getWriteAble();

        for (String name : fieldName) {
            if (StringUtils.isBlank(name)) {
                continue;
            }

            final PropertyDescriptor descriptor = descriptorsMap.get(name);
            // 优先使用自省
            if (descriptor != null) {
                final Object value = provider.convert(fieldValue, descriptor.getPropertyType());
                try {
                    final Method method = descriptor.getWriteMethod();
                    method.setAccessible(true);
                    method.invoke(object, value);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new EasyDataConvertException(e);
                }
            } else {
                // 字段反射兜底
                final Field field = PropertyCache.tryGetField(target, name);
                if (field != null) {
                    final Object value = provider.convert(fieldValue, field.getType());
                    Whitebox.setInternalState(object, name, value);
                }
            }
        }
    }
}
