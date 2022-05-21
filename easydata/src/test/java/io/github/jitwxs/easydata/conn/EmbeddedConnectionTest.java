package io.github.jitwxs.easydata.conn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import io.github.jitwxs.easydata.common.enums.FileFormatEnum;
import io.github.jitwxs.easydata.core.loader.EasyLoader;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmbeddedConnectionTest {
    @Test
    @DisplayName("读取 JSON")
    public void testConnectionJson1() {
        final EmbeddedConnection connection = new EmbeddedConnection(FileFormatEnum.JSON);

        final LoaderProperties properties = LoaderProperties.builder()
                .url("{\"name\": \"zhangsan\", \"age\":  20, \"hasMale\":  true}")
                .build();

        final EasyLoader loader = new EasyLoader(connection);

        final List<TestBean> beanList = loader.loading(TestBean.class, properties);

        assertEquals(1, beanList.size());
        EasyVerify.with(TestBean.builder().name("zhangsan").age(20).hasMale(true).build(), beanList.get(0)).verify();
    }

    @Test
    @DisplayName("读取 JSON | 自定义反序列化")
    public void testConnectionJson2() {
        final EmbeddedConnection connection = new EmbeddedConnection(FileFormatEnum.JSON);

        final LoaderProperties properties = LoaderProperties.builder()
                .url("{\"n\": \"zhangsan\", \"a\":  20, \"s\":  true}")
                .jsonDeserializeFunc(e -> {
                    final JSONObject object = JSON.parseObject(e);
                    return TestBean.builder().name(object.getString("n")).age(object.getInteger("a")).hasMale(object.getBoolean("s")).build();
                })
                .build();

        final EasyLoader loader = new EasyLoader(connection);

        final List<TestBean> beanList = loader.loading(TestBean.class, properties);

        assertEquals(1, beanList.size());
        EasyVerify.with(TestBean.builder().name("zhangsan").age(20).hasMale(true).build(), beanList.get(0)).verify();
    }

    @Test
    @DisplayName("读取 CSV")
    public void testConnectionCsv1() {
        final EmbeddedConnection connection = new EmbeddedConnection(FileFormatEnum.CSV);

        final LoaderProperties properties = LoaderProperties.builder()
                .url("name,age,hasMale\n" +
                        "Pepito,18,true\n" +
                        "MacShane,25,false")
                .build();

        final EasyLoader loader = new EasyLoader(connection);

        final List<TestBean> beanList = loader.loading(TestBean.class, properties);

        assertEquals(2, beanList.size());
        EasyVerify.with(Sets.newHashSet(
                TestBean.builder().name("Pepito").age(18).hasMale(true).build(),
                TestBean.builder().name("MacShane").age(25).hasMale(false).build()
        ), new HashSet<>(beanList)).verify();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestBean {
        private String name;

        private int age;

        private boolean hasMale;
    }
}