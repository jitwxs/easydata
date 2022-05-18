package io.github.jitwxs.easydata.sample.core.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import io.github.jitwxs.easydata.sample.bean.UserInfo;
import net.bytebuddy.ByteBuddy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static io.github.jitwxs.easydata.core.loader.EasyLoader.FILE_LOADER;
import static io.github.jitwxs.easydata.core.mock.EasyMock.run;
import static io.github.jitwxs.easydata.core.verify.EasyVerify.with;
import static org.junit.jupiter.api.Assertions.*;

public class LoaderTest {
    @Test
    @DisplayName("加载 CSV 数据 | 额外字段")
    public void loadingCsvWithExtraField() {
        final String addedField = run(String.class);

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
                    .url("/easydata/loader/order_evaluate.csv")
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
        final LoaderProperties properties = LoaderProperties.builder().url("/easydata/loader/user_info_one.json").build();

        final List<UserInfo> userInfos = FILE_LOADER.loading(UserInfo.class, properties);

        assertNotNull(userInfos);
        assertEquals(1, userInfos.size());
    }

    @Test
    @DisplayName("加载 JSON 对象 | 额外字段")
    public void loadingJsonObjectWithExtraField() {
        final String addedField = run(String.class);

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
                    .url("/easydata/loader/user_info_one.json")
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

    @Test
    @DisplayName("加载 JSON 对象 | 自定义反序列化策略")
    public void loadingJsonObjectWithCustomDeserialize() {
        final UserInfo userInfo = UserInfo.builder()
                ._id("zhangsan")
                .age(19)
                .phone("110120119911")
                .build();

        Function<String, UserInfo> deserializeFunc = str -> {
            final JSONObject jsonObject = JSON.parseObject(str);

            return UserInfo.builder()
                    ._id(jsonObject.getString("0"))
                    .age(jsonObject.getInteger("1"))
                    .phone(jsonObject.getString("2"))
                    .build();
        };

        final LoaderProperties properties = LoaderProperties.builder()
                .url("/easydata/loader/user_info_deserialize.json")
                .jsonDeserializeFunc(deserializeFunc)
                .build();

        final List<UserInfo> infoList = FILE_LOADER.loading(UserInfo.class, properties);

        assertEquals(1, infoList.size());
        with(userInfo, infoList.get(0)).verify();
    }
}