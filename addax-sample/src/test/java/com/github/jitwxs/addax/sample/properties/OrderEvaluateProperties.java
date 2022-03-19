package com.github.jitwxs.addax.sample.properties;

import com.github.jitwxs.addax.common.bean.LoaderPropertiesUrl;
import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 22:39
 */
public class OrderEvaluateProperties implements LoaderProperties {
    @Override
    public Class<?> clazz() {
        return OrderEvaluate.class;
    }

    @Override
    public LoaderPropertiesUrl url() {
        return LoaderPropertiesUrl.builder()
                .fileConn("/addax/loader/order_evaluate.csv")
                .sqlConn("SELECT * FROM order_evaluate")
                .build();
    }

    @Override
    public List<Pair<String, String>> extraFieldMappings() {
        return null;
    }
}
