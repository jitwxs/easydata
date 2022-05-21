package io.github.jitwxs.easydata.common.bean;

import io.github.jitwxs.easydata.common.exception.EasyDataException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-21 0:09
 */
@Getter
public class ClassProperty {
    private final Class<?> target;

    private final Map<String, FieldProperty> all = new HashMap<>();

    private final Map<String, FieldProperty> readable = new HashMap<>();

    private final Map<String, FieldProperty> writeAble = new HashMap<>();

    private final Map<String, FieldProperty> readAndWriteAble = new HashMap<>();

    public ClassProperty(Class<?> clazz) {
        this.target = clazz;

        try {
            final Map<String, FieldProperty> propertyMap = FieldProperty.newInstance(this.target);

            propertyMap.forEach((k, v) -> {
                this.all.put(k, v);

                if (v.isReadable()) {
                    this.readable.put(k, v);
                }

                if (v.isWriteable()) {
                    this.writeAble.put(k, v);
                }

                if (v.isReadable() && v.isWriteable()) {
                    this.readAndWriteAble.put(k, v);
                }
            });
        } catch (Exception e) {
            throw new EasyDataException(String.format("Cache Class %s Property Failed", clazz), e);
        }
    }
}
