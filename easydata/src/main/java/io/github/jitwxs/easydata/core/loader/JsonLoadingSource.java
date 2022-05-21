package io.github.jitwxs.easydata.core.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.DataTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 12:04
 */
@Slf4j
public class JsonLoadingSource extends LoadingSource<List<String>> {
    public JsonLoadingSource(LoaderProperties properties, List<String> source) {
        super(DataTypeEnum.JSON, properties, source);
    }

    @Override
    public <K> List<K> toBean(Class<K> target, LoaderProperties properties) {
        if (source.size() == 0) {
            return Collections.emptyList();
        }

        final HashBiMap<String, String> extraFieldMap = HashBiMap.create(super.initialExtraFields(properties));

        // json > bean
        final Function<String, K> deserializeFunc;
        if (properties.getJsonDeserializeFunc() != null) {
            deserializeFunc = (Function<String, K>) properties.getJsonDeserializeFunc();
        } else {
            final ExtraResolve extraResolve = new ExtraResolve(target, extraFieldMap, this);
            deserializeFunc = e -> JSON.parseObject(e, target, extraResolve);
        }

        final List<K> resultList = this.source.stream().map(deserializeFunc).collect(Collectors.toList());

        // 实体字段 > json字段
        if (!extraFieldMap.isEmpty()) {
            final Iterator<Map.Entry<String, String>> iterator = extraFieldMap.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, String> entry = iterator.next();

                // 存在无效的字段
                final Map<String, FieldProperty> propertyMap = PropertyCache.tryGet(target).getAll();
                if (propertyMap.get(entry.getKey()) == null || propertyMap.get(entry.getValue()) == null) {
                    iterator.remove();
                    continue;
                }

                for (K one : resultList) {
                    final Object value1 = Whitebox.getInternalState(one, entry.getKey());
                    final Object value2 = Whitebox.getInternalState(one, entry.getValue());

                    // 都没值，无需映射
                    if (value1 == null && value2 == null) {
                        iterator.remove();
                        continue;
                    }

                    // 字段映射
                    if (value1 != null) {
                        fillingField(one, value1, new String[]{entry.getValue()});
                    }
                    if (value2 != null) {
                        fillingField(one, value2, new String[]{entry.getKey()});
                    }

                    iterator.remove();
                }

            }

        }

        return resultList;
    }

    @Data
    static class ExtraResolve implements ExtraProcessor, ExtraTypeProvider {
        private final Class<?> target;

        private final BiMap<String, String> extraFiledMap;

        private final BiMap<String, String> extraFiledMap2;

        private final JsonLoadingSource loadingSource;

        public ExtraResolve(Class<?> target, BiMap<String, String> extraFiledMap, JsonLoadingSource loadingSource) {
            this.target = target;
            this.extraFiledMap = extraFiledMap;
            this.extraFiledMap2 = extraFiledMap.inverse();
            this.loadingSource = loadingSource;
        }

        @Override
        public void processExtra(Object object, String key, Object value) {
            String mappingField = extraFiledMap.get(key);
            if (mappingField != null) {
                loadingSource.fillingField(object, value, new String[]{mappingField});

                extraFiledMap.remove(key);
                return;
            }

            mappingField = extraFiledMap2.get(key);
            if (mappingField != null) {
                loadingSource.fillingField(object, value, new String[]{mappingField});

                extraFiledMap2.remove(key);
            }
        }

        @Override
        public Type getExtraType(Object object, String key) {
            final String mappingField = extraFiledMap.getOrDefault(key, extraFiledMap2.get(key));
            if (mappingField == null) {
                return null;
            }

            final FieldProperty property = PropertyCache.tryGet(target, mappingField);
            if (property == null) {
                return null;
            }

            return property.getType();
        }
    }
}
