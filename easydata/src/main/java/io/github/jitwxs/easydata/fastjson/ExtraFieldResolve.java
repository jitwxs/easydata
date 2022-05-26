package io.github.jitwxs.easydata.fastjson;

import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.google.common.collect.BiMap;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.core.loader.JsonLoadingSource;
import lombok.Data;

import java.lang.reflect.Type;

@Data
public class ExtraFieldResolve implements ExtraProcessor, ExtraTypeProvider {
    private final Class<?> target;

    private final BiMap<String, String> extraFiledMap;

    private final BiMap<String, String> extraFiledMap2;

    private final JsonLoadingSource loadingSource;

    public ExtraFieldResolve(Class<?> target, BiMap<String, String> extraFiledMap, JsonLoadingSource loadingSource) {
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