package com.github.jitwxs.addax.core.loader;

import com.github.jitwxs.addax.core.mock.Mock;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import com.github.jitwxs.addax.sample.bean.UserInfo;
import net.bytebuddy.ByteBuddy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static com.github.jitwxs.addax.core.loader.Loader.FILE_LOADER;
import static org.junit.jupiter.api.Assertions.*;

public class LoaderTest {
    @Test
    @DisplayName("加载 CSV 数据 | 额外字段")
    public void loadingCsvWithExtraField() {
        final String addedField = Mock.run(String.class);

        // 构造子类，加一个字段
        final Class<? extends OrderEvaluate> target = new ByteBuddy()
                .subclass(OrderEvaluate.class)
                .defineField(addedField, String.class, Modifier.PUBLIC)
                .make()
                .load(getClass().getClassLoader()).getLoaded();

        for (String[] extraFields : Arrays.asList(
                null, // 不指定字段映射
                new String[]{addedField, "id"}, // 字段映射1
                new String[]{"id", addedField} // 字段映射2
        )) {
            final LoaderProperties properties = LoaderProperties.builder()
                    .url("/addax/loader/order_evaluate.csv")
                    .extraFields(extraFields)
                    .build();

            final List<? extends OrderEvaluate> evaluates = FILE_LOADER.loading(target, properties);

            final Object result = evaluates.get(0);
            assertEquals(result.getClass(), target);

            if (extraFields == null) {
                assertNull(Whitebox.getInternalState(result, addedField));
            } else {
                final Object extraValue = Whitebox.getInternalState(result, addedField);
                assertNotNull(extraValue);
                assertEquals(extraValue, Whitebox.getInternalState(result, "id"));
            }
        }
    }

    @Test
    @DisplayName("加载 JSON 对象")
    public void loadingJsonObject() {
        final LoaderProperties properties = LoaderProperties.builder().url("/addax/loader/user_info_one.json").build();

        final List<UserInfo> userInfos = FILE_LOADER.loading(UserInfo.class, properties);

        assertNotNull(userInfos);
        assertEquals(1, userInfos.size());
    }

    @Test
    @DisplayName("加载 JSON 对象 | 额外字段")
    public void loadingJsonObjectWithExtraField() {
        final String addedField = Mock.run(String.class);

        // 构造子类，加一个字段
        final Class<? extends UserInfo> target = new ByteBuddy()
                .subclass(UserInfo.class)
                .defineField(addedField, Integer.class, Modifier.PUBLIC)
                .make()
                .load(getClass().getClassLoader()).getLoaded();

        for (String[] extraFields : Arrays.asList(
                null, // 不指定字段映射
                new String[]{addedField, "age"}, // 字段映射1
                new String[]{"age", addedField} // 字段映射2
        )) {
            final LoaderProperties properties = LoaderProperties.builder()
                    .url("/addax/loader/user_info_one.json")
                    .extraFields(extraFields)
                    .build();

            final List<? extends UserInfo> objects = FILE_LOADER.loading(target, properties);

            final Object result = objects.get(0);
            assertEquals(result.getClass(), target);

            if (extraFields == null) {
                assertNull(Whitebox.getInternalState(result, addedField));
            } else {
                final Object extraValue = Whitebox.getInternalState(result, addedField);
                assertNotNull(extraValue);
                assertEquals(extraValue, Whitebox.getInternalState(result, "age"));
            }
        }
    }
}