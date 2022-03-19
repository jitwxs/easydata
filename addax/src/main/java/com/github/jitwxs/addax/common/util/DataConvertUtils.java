package com.github.jitwxs.addax.common.util;

import com.alibaba.fastjson.JSONObject;
import com.github.jitwxs.addax.common.bean.MatrixBean;
import com.github.jitwxs.addax.common.cache.PropertyCache;
import com.github.jitwxs.addax.common.exception.AddaxConvertException;
import com.github.jitwxs.addax.provider.ConvertProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.tuple.Pair;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * read pair content, and convert to java bean
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:54
 */
public class DataConvertUtils {
    /**
     * @param clazz             目标类型
     * @param extraFiledMapping 额外的字段映射
     */
    public static <T> List<T> matrixToList(final MatrixBean matrixBean, final Class<T> clazz, Collection<Pair<String, String>> extraFiledMapping) {
        if (MatrixBean.ignore(matrixBean)) {
            return Collections.emptyList();
        }

        final BiMap<String, String> extraFiled1 = HashBiMap.create();
        extraFiledMapping.forEach(pair -> extraFiled1.put(pair.getLeft(), pair.getRight()));
        final BiMap<String, String> extraFiled2 = extraFiled1.inverse();

        final String[] dataTitles = matrixBean.getTitle();
        final Map<String, PropertyDescriptor> descriptorsMap = PropertyCache.tryGet(clazz).getWriteAble();

        final ConvertProvider provider = ProviderFactory.delegate(ConvertProvider.class);

        final List<T> results = new ArrayList<>();

        for (String[] lines : matrixBean.getDataList()) {
            final T one = ObjectUtils.create(clazz);

            for (int i = 0; i < lines.length; i++) {
                final String lineKey = dataTitles[i], lineValue = lines[i];

                final List<PropertyDescriptor> descriptors = Stream.of(
                        LineHumpUtils.lineToHump(lineKey), // 驼峰映射
                        extraFiled1.get(lineKey), // 额外映射-1
                        extraFiled2.get(lineKey) // 额外映射-2
                ).filter(Objects::nonNull).map(descriptorsMap::get).filter(Objects::nonNull).collect(Collectors.toList());

                for (PropertyDescriptor descriptor : descriptors) {
                    final Object value = provider.convert(lineValue, descriptor.getPropertyType());
                    if (value != null) {
                        try {
                            final Method method = descriptor.getWriteMethod();
                            method.setAccessible(true);
                            method.invoke(one, value);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            throw new AddaxConvertException(e);
                        }
                    }
                }
            }

            results.add(one);
        }

        return results;
    }

    public static <T> List<T> jsonToList(final List<String[]> dataList, final Class<T> clazz, Collection<Pair<String, String>> extraFiledMapping) {
        return dataList.stream().flatMap(Arrays::stream).map(e -> JSONObject.parseObject(e, clazz)).collect(Collectors.toList());
    }
}
