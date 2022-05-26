package io.github.jitwxs.easydata.core.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import lombok.extern.slf4j.Slf4j;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-26 22:19
 */
@Slf4j
public class MapEntryConvert implements IConvert<Map.Entry> {
    @Override
    public Map.Entry convert(Object source) throws EasyDataConvertException {
        throw new EasyDataConvertException("Not support operation");
    }

    @Override
    public Map.Entry convert(Object source, Class<?> target) throws EasyDataConvertException {
        final Class<?> sourceClass = source.getClass();

        final Map.Entry entry;
        if (sourceClass == String.class) {
            entry = fromString((String) source);
        } else if (Map.Entry.class.isAssignableFrom(sourceClass)) {
            entry = (Map.Entry) source;
        } else {
            throw new EasyDataConvertException("Not support convert source class: " + sourceClass);
        }

        try {
            final Method method = Whitebox.getMethod(target, "of", Map.Entry.class);
            if (method != null) {
                return Whitebox.invokeMethod(target, method, entry);
            }
        } catch (Exception ignored) {
        }

        try {
            final Method method = Whitebox.getMethod(target, "of", Object.class, Object.class);
            if (method != null) {
                return Whitebox.invokeMethod(target, method, entry.getKey(), entry.getValue());
            }
        } catch (Exception ignored) {
        }

        throw new EasyDataConvertException("Not support suit method to invoke convert");
    }

    private Map.Entry fromString(final String string) {
        final JSONObject jsonObject = JSON.parseObject(string);

        final String key = jsonObject.keySet().iterator().next();
        final String value = jsonObject.getString(key);

        final HashMap<String, String> map = new HashMap<String, String>() {{
            put(key, value);
        }};

        return map.entrySet().iterator().next();
    }
}
